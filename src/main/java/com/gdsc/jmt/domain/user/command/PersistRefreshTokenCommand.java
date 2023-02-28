package com.gdsc.jmt.domain.user.command;

import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class PersistRefreshTokenCommand extends BaseCommand<String> {
    private final String email;
    private final String refreshToken;

    public PersistRefreshTokenCommand(String id, String email, String refreshToken) {
        super(id);
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
