package com.gdsc.jmt.domain.user.command;

import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class PersistRefreshTokenCommand extends BaseCommand<String> {
    private final String userId;
    private final String refreshToken;

    public PersistRefreshTokenCommand(String id, String userId, String refreshToken) {
        super(id);
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
