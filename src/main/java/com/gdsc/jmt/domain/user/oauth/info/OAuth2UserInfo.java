package com.gdsc.jmt.domain.user.oauth.info;

import com.gdsc.jmt.domain.user.common.RoleType;
import com.gdsc.jmt.domain.user.common.SocialType;
import com.gdsc.jmt.domain.user.common.Status;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;

import java.util.Map;

public abstract class OAuth2UserInfo {
    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

    public abstract SocialType getSocialType();


    public UserEntity createUserEntity() {
        UserEntity user = new UserEntity();
        user.setEmail(getEmail());
        user.setSocialType(getSocialType());
        user.setRoleType(RoleType.MEMBER);
        user.setStatus(Status.ACTIVE);
        return user;
    }
}
