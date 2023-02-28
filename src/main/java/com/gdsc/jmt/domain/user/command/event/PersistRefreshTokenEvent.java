package com.gdsc.jmt.domain.user.command.event;

import lombok.Getter;

@Getter
public class PersistRefreshTokenEvent extends BaseRefreshTokenEvent<String> {

    private final String email;
    private final String refreshToken;

    public PersistRefreshTokenEvent(String id, String email, String refreshToken) {
        super(id);
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
