package com.gdsc.jmt.domain.user.command.event;

import lombok.Getter;

@Getter
public class UpdateUserProfileImgEvent extends BaseUserEvent<String> {
    private final String profileImageUrl;
    public UpdateUserProfileImgEvent(String id, String profileImageUrl) {
        super(id);
        this.profileImageUrl = profileImageUrl;
    }
}
