package com.gdsc.jmt.domain.group.command.service;

import com.gdsc.jmt.domain.group.code.GroupUserRole;
import com.gdsc.jmt.domain.group.command.controller.request.CreateGroupRequest;
import com.gdsc.jmt.domain.group.command.controller.response.FindGroupResponse;
import com.gdsc.jmt.domain.group.command.controller.response.FindGroupResponseItem;
import com.gdsc.jmt.domain.group.entity.GroupEntity;
import com.gdsc.jmt.domain.group.entity.GroupUserSelectEntity;
import com.gdsc.jmt.domain.group.entity.GroupUsersEntity;
import com.gdsc.jmt.domain.group.repository.GroupRepository;
import com.gdsc.jmt.domain.group.repository.GroupUserRepository;
import com.gdsc.jmt.domain.group.repository.GroupUserSelectRepository;
import com.gdsc.jmt.domain.restaurant.query.dto.PageMeta;
import com.gdsc.jmt.domain.restaurant.query.repository.RecommendRestaurantRepository;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
import com.gdsc.jmt.global.dto.PageResponse;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.jwt.dto.UserInfo;
import com.gdsc.jmt.global.messege.GroupMessage;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import com.gdsc.jmt.global.messege.UserMessage;
import com.gdsc.jmt.global.service.S3FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;

    private final RecommendRestaurantRepository recommendRestaurantRepository;
    private final GroupUserSelectRepository groupUserSelectRepository;

    private final S3FileService s3FileService;

    @Transactional
    public Long createGroup(CreateGroupRequest request, UserInfo userInfo) {
        Optional<UserEntity> userResult = userRepository.findByEmail(userInfo.getEmail());
        if(userResult.isEmpty()) {
            throw new ApiException(UserMessage.USER_NOT_FOUND);
        }
        UserEntity userEntity = userResult.get();

        GroupEntity.GroupEntityBuilder groupEntityBuilder = GroupEntity.builder()
                .groupName(request.groupName)
                .groupIntroduce(request.groupIntroduce)
                .privateFlag(request.isPrivateGroup);
        this.uploadImages(groupEntityBuilder, request.getGroupProfileImage(), request.getGroupBackgroundImage());
        GroupEntity groupEntity = groupEntityBuilder.build();
        groupRepository.save(groupEntity);

        GroupUserRole ownerRole = GroupUserRole.OWNER;
        GroupUsersEntity groupUsersEntity = GroupUsersEntity.builder()
                .groupId(groupEntity.getGid())
                .userId(userEntity.getId())
                .role(ownerRole)
                .build();
        groupUserRepository.save(groupUsersEntity);

        return groupEntity.getGid();
    }

    @Transactional(readOnly = true)
    public FindGroupResponseItem findGroupById(long groupId) {
        Optional<GroupEntity> groupEntityResult = groupRepository.findById(groupId);
        if(groupEntityResult.isEmpty()) {
            throw new ApiException(GroupMessage.GROUP_NOT_FOUND);
        }
        GroupEntity groupEntity = groupEntityResult.get();

        int memberCnt = groupUserRepository.countByGroupId(groupId);
        int restaurantCnt = recommendRestaurantRepository.countByGroup(groupEntity);

        return FindGroupResponseItem.builder()
                .groupId(groupEntity.getGid())
                .groupName(groupEntity.getGroupName())
                .groupIntroduce(groupEntity.getGroupIntroduce())
                .isPrivateGroup(groupEntity.isPrivateFlag())
                .groupBackgroundImageUrl(groupEntity.getGroupBackgroundImageUrl())
                .groupProfileImageUrl(groupEntity.getGroupProfileImageUrl())
                .memberCnt(memberCnt)
                .restaurantCnt(restaurantCnt)
                .build();
    }

    @Transactional(readOnly = true)
    public List<FindGroupResponseItem> findUserGroupList(UserInfo user) {
        Optional<UserEntity> userResult = userRepository.findByEmail(user.getEmail());
        if(userResult.isEmpty()) {
             throw new ApiException(UserMessage.USER_NOT_FOUND);
        }

        List<Long> groupIds = groupUserRepository.findByUserId(userResult.get().getId())
                .stream()
                .map(GroupUsersEntity::getGroupId)
                .toList();
        List<GroupEntity> groupEntities = groupRepository.findByGidIn(groupIds);

        Optional<GroupUserSelectEntity> groupUserSelectResult = groupUserSelectRepository.findByUserId(userResult.get().getId());
        return groupEntities
                .stream()
                .map(group -> {
                    boolean isSelected = groupUserSelectResult
                            .map(groupUserSelectEntity ->
                                    groupUserSelectEntity.groupId.equals(group.gid)).orElse(false);
                    int memberCnt = groupUserRepository.countByGroupId(group.getGid());
                    int restaurantCnt = recommendRestaurantRepository.countByGroup(group);
                    return group.toResponse(memberCnt, restaurantCnt, isSelected);
                })
                .toList();
    }

    @Transactional
    public void leaveGroup(long groupId, UserInfo user) {
        Optional<UserEntity> userResult = userRepository.findByEmail(user.getEmail());
        if(userResult.isEmpty()) {
            throw new ApiException(UserMessage.USER_NOT_FOUND);
        }
        Optional<GroupUsersEntity> groupUserResult = groupUserRepository.findByGroupIdAndUserId(groupId, userResult.get().getId());

        if(groupUserResult.isEmpty()) {
            throw new ApiException(GroupMessage.USER_NOT_FOUND_IN_GROUP);
        }
        groupUserRepository.delete(groupUserResult.get());
    }

    @Transactional
    public void joinGroup(long groupId, UserInfo user) {
        Optional<UserEntity> userResult = userRepository.findByEmail(user.getEmail());
        if(userResult.isEmpty()) {
            throw new ApiException(UserMessage.USER_NOT_FOUND);
        }

        Optional<GroupEntity> groupResult = groupRepository.findById(groupId);
        if(groupResult.isEmpty()) {
            throw new ApiException(GroupMessage.GROUP_NOT_FOUND);
        }

        List<GroupUsersEntity> groupUsers = groupUserRepository.findByUserId(userResult.get().getId());
        boolean isAlreadyExistsGroup = groupUsers
                .stream()
                .anyMatch(groupUser -> groupUser.getGroupId().equals(groupId));
        if(isAlreadyExistsGroup) {
            throw new ApiException(GroupMessage.USER_ALREADY_EXISTS_IN_GROUP);
        }

        GroupUsersEntity joinGroupUsersEntity = GroupUsersEntity.builder()
                .userId(userResult.get().getId())
                .groupId(groupId)
                .role(GroupUserRole.MEMBER)
                .build();

        if(groupUsers.isEmpty()) {
            GroupUserSelectEntity groupUserSelectEntity = GroupUserSelectEntity.builder()
                    .groupId(groupId)
                    .userId(userResult.get().getId())
                    .build();
            groupUserSelectRepository.save(groupUserSelectEntity);
        }

        groupUserRepository.save(joinGroupUsersEntity);
    }

    private void uploadImages(GroupEntity.GroupEntityBuilder groupEntityBuilder, MultipartFile groupProfileImage, MultipartFile groupBackgroundImage) {
        try {
            if(groupProfileImage != null) {
                String groupProfileImageUrl = s3FileService.upload(groupProfileImage,"groupProfileUser");
                groupEntityBuilder.groupProfileImageUrl(groupProfileImageUrl);
            }
            else if(groupBackgroundImage != null) {
                String groupBackgroundImageUrl = s3FileService.upload(groupBackgroundImage,"groupBackgroundUser");
                groupEntityBuilder.groupBackgroundImageUrl(groupBackgroundImageUrl);
            }
        }
        catch (IOException e) {
            throw new ApiException(RestaurantMessage.RESTAURANT_IMAGE_UPLOAD_FAIL);
        }

    }

    @Transactional
    public void userSelectGroup(Long groupId, UserInfo user) {
        Optional<UserEntity> userResult = userRepository.findByEmail(user.getEmail());
        if(userResult.isEmpty()) {
            throw new ApiException(UserMessage.USER_NOT_FOUND);
        }
        Optional<GroupEntity> groupEntityResult = groupRepository.findById(groupId);
        if(groupEntityResult.isEmpty()) {
            throw new ApiException(GroupMessage.GROUP_NOT_FOUND);
        }

        Optional<GroupUserSelectEntity> result = groupUserSelectRepository.findByUserId(userResult.get().getId());

        if(result.isPresent()) {
            groupUserSelectRepository.delete(result.get());
        }

        GroupUserSelectEntity groupUserSelectEntity = GroupUserSelectEntity.builder()
                .groupId(groupId)
                .userId(userResult.get().getId())
                .build();
        groupUserSelectRepository.save(groupUserSelectEntity);
    }

    public FindGroupResponse searchByGroupName(String keyword, Pageable pageable) {
        Page<GroupEntity> result = groupRepository.findByGroupName(keyword, pageable);
        if(result.isEmpty()) {
            throw new ApiException(GroupMessage.GROUP_NOT_FOUND);
        }
        PageResponse pageResponse = new PageResponse(result);
        return new FindGroupResponse(
                result.getContent().stream().map(group -> {
                    int memberCnt = groupUserRepository.countByGroupId(group.getGid());
                    int restaurantCnt = recommendRestaurantRepository.countByGroup(group);
                    return group.toFindGroupTitleResponse(memberCnt, restaurantCnt);
                }).toList(),
                pageResponse
        );
    }
}
