package com.gdsc.jmt.domain.user.command;

import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class PersistRefreshTokenCommand extends BaseCommand<String> {
    private final Long userId;
    private final String refreshToken;

    public PersistRefreshTokenCommand(String id, Long userId, String refreshToken) {
        super(id);
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
