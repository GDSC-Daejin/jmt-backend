package com.gdsc.jmt.domain.user.command;

import com.gdsc.jmt.domain.user.common.SocialType;
import com.gdsc.jmt.domain.user.oauth.info.OAuth2UserInfo;
import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class SignUpCommand extends BaseCommand<String> {
    private final OAuth2UserInfo userInfo;
    private final SocialType socialType;

    public SignUpCommand(String id, OAuth2UserInfo userInfo, SocialType socialType) {
        super(id);
        this.userInfo = userInfo;
        this.socialType = socialType;
    }
}
