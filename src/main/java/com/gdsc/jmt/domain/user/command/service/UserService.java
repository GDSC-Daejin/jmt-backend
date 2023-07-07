package com.gdsc.jmt.domain.user.command.service;

import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.UserMessage;
import com.gdsc.jmt.global.service.S3FileService;
import java.io.IOException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3FileService s3FileService;

    private final String DEFAULT_PROFILE_IMAGE_URL = "https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png";

    @Transactional
    public void updateUserNickName(String email, String nickName) {
        Optional<UserEntity> userEntity = userRepository.findByNickname(nickName);
        if(userEntity.isPresent()) {
            throw new ApiException(UserMessage.NICKNAME_IS_DUPLICATED);
        }

        Optional<UserEntity> result = userRepository.findByEmail(email);
        if(result.isEmpty()) {
            throw new ApiException(UserMessage.USER_NOT_FOUND);
        }

        UserEntity updateUserEntity = result.get();
        updateUserEntity.setNickname(nickName);
        userRepository.save(updateUserEntity);
    }

    @Transactional
    public String updateUserProfileImg(String email, MultipartFile profileImg) {
        String responseUrl = uploadProfileImage(profileImg);
        updateProfileImage(email, responseUrl);
        return responseUrl;
    }

    @Transactional
    public String updateUserDefaultProfileImg(String email) {
        updateProfileImage(email, DEFAULT_PROFILE_IMAGE_URL);
        return DEFAULT_PROFILE_IMAGE_URL;
    }

    private String uploadProfileImage(MultipartFile profileImg) {
        String responseUrl;
        try {
            responseUrl = s3FileService.upload(profileImg, "profileImg");
        } catch (IOException e) {
            throw new ApiException(UserMessage.PROFILE_IMAGE_UPLOAD_FAIL);
        }
        return responseUrl;
    }

    private void updateProfileImage(String email, String imageUrl) {
        Optional<UserEntity> result = userRepository.findByEmail(email);
        UserEntity updateUserEntity = result.orElse(null);
        if(result.isEmpty()) {
            throw new ApiException(UserMessage.USER_NOT_FOUND);
        }

        if(updateUserEntity.getProfileImageUrl() != null) {
            s3FileService.delete(updateUserEntity.getProfileImageUrl());
        }
        updateUserEntity.setProfileImageUrl(imageUrl);
        userRepository.save(updateUserEntity);
    }
}