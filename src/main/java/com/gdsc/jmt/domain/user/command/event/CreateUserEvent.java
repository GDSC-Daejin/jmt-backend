package com.gdsc.jmt.domain.user.command.event;

import com.gdsc.jmt.domain.user.common.SocialType;
import lombok.Getter;

@Getter
public class CreateUserEvent extends BaseUserEvent<String> {
    private final String email;
    private final SocialType socialType;

    public CreateUserEvent(String id, String email, SocialType socialType) {
        super(id);
        this.email = email;
        this.socialType = socialType;
    }
}
