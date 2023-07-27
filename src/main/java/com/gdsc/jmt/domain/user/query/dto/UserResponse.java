package com.gdsc.jmt.domain.user.query.dto;

import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import lombok.Builder;

public record UserResponse(
        String email,
        String nickname,
        String profileImg ) {
    public UserResponse(UserEntity userEntity) {
        this(userEntity.getEmail(), userEntity.getNickname(), userEntity.getProfileImageUrl());
    }
}
