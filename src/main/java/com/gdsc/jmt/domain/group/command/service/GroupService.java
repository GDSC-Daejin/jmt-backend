package com.gdsc.jmt.domain.group.command.service;

import com.gdsc.jmt.domain.group.code.GroupUserRole;
import com.gdsc.jmt.domain.group.command.controller.request.CreateGroupRequest;
import com.gdsc.jmt.domain.group.entity.GroupEntity;
import com.gdsc.jmt.domain.group.entity.GroupUsersEntity;
import com.gdsc.jmt.domain.group.repository.GroupRepository;
import com.gdsc.jmt.domain.group.repository.GroupUserRepository;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantPhotoEntity;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.jwt.dto.UserInfo;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import com.gdsc.jmt.global.messege.UserMessage;
import com.gdsc.jmt.global.service.S3FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                .gId(groupEntity.getGid())
                .userId(userEntity.getId())
                .role(ownerRole)
                .build();
        groupUserRepository.save(groupUsersEntity);

        return groupEntity.getGid();
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
