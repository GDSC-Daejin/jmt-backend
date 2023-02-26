package com.gdsc.jmt.domain.user.command.event;

import lombok.Getter;

@Getter
public class PersistRefreshTokenEvent extends BaseRefreshTokenEvent<String> {

    private final Long userId;
    private final String refreshToken;

    public PersistRefreshTokenEvent(String id, Long userId, String refreshToken) {
        super(id);
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
