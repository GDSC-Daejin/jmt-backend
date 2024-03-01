package com.gdsc.jmt.domain.group.command.service;

import com.gdsc.jmt.domain.group.code.GroupUserRole;
import com.gdsc.jmt.domain.group.command.controller.request.CreateGroupRequest;
import com.gdsc.jmt.domain.group.command.controller.response.FindGroupResponse;
import com.gdsc.jmt.domain.group.entity.GroupEntity;
import com.gdsc.jmt.domain.group.entity.GroupUsersEntity;
import com.gdsc.jmt.domain.group.repository.GroupRepository;
import com.gdsc.jmt.domain.group.repository.GroupUserRepository;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantPhotoEntity;
import com.gdsc.jmt.domain.restaurant.query.repository.RecommendRestaurantRepository;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.jwt.dto.UserInfo;
import com.gdsc.jmt.global.messege.GroupMessage;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import com.gdsc.jmt.global.messege.UserMessage;
import com.gdsc.jmt.global.service.S3FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    public FindGroupResponse findGroupById(long groupId) {
        Optional<GroupEntity> groupEntityResult = groupRepository.findById(groupId);
        if(groupEntityResult.isEmpty()) {
            throw new ApiException(GroupMessage.GROUP_NOT_FOUND);
        }
        GroupEntity groupEntity = groupEntityResult.get();

        int memberCnt = groupUserRepository.countByGroupId(groupId);
        int restaurantCnt = recommendRestaurantRepository.countByGroup(groupEntity);

        return FindGroupResponse.builder()
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
    public List<FindGroupResponse> findUserGroupList(UserInfo user) {
        Optional<UserEntity> userResult = userRepository.findByEmail(user.getEmail());
        if(userResult.isEmpty()) {
             throw new ApiException(UserMessage.USER_NOT_FOUND);
        }

        List<Long> groupIds = groupUserRepository.findByUserId(userResult.get().getId())
                .stream()
                .map(GroupUsersEntity::getGroupId)
                .toList();
        List<GroupEntity> groupEntities = groupRepository.findByGidIn(groupIds);

        return groupEntities
                .stream()
                .map(GroupEntity::toResponse)
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

        Optional<GroupUsersEntity> groupUserResult = groupUserRepository.findByGroupIdAndUserId(groupId, userResult.get().getId());
        if(groupUserResult.isPresent()) {
            throw new ApiException(GroupMessage.USER_ALREADY_EXISTS_IN_GROUP);
        }

        GroupUsersEntity joinGroupUsersEntity = GroupUsersEntity.builder()
                .userId(userResult.get().getId())
                .groupId(groupId)
                .role(GroupUserRole.MEMBER)
                .build();

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
}
