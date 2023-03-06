package com.gdsc.jmt.domain.user.command;

import com.gdsc.jmt.domain.user.oauth.info.impl.GoogleOAuth2UserInfo;
import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class GoogleLoginCommand extends BaseCommand<String> {
    private final GoogleOAuth2UserInfo userInfo;

    public GoogleLoginCommand(String id, GoogleOAuth2UserInfo userInfo) {
        super(id);
        this.userInfo = userInfo;
    }
}
