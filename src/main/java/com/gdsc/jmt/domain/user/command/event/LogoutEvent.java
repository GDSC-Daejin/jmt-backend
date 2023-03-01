package com.gdsc.jmt.domain.user.command.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class LogoutEvent extends BaseRefreshTokenEvent<String>{
    private final String email;
    private final String refreshToken;

    public LogoutEvent(String id, String email, String refreshToken) {
        super(id);
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
