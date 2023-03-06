package com.gdsc.jmt.domain.user.command.event;

import lombok.Getter;

@Getter
public class PersistRefreshTokenEvent extends BaseRefreshTokenEvent<String> {

    private final String userId;
    private final String refreshToken;

    public PersistRefreshTokenEvent(String id, String userId, String refreshToken) {
        super(id);
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
