package com.gdsc.jmt.domain.user.command;

import com.gdsc.jmt.domain.user.command.info.Reissue;
import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class PersistRefreshTokenCommand extends BaseCommand<String> {
    private final String email;
    private final String refreshToken;

    private final Reissue reissue;

    public PersistRefreshTokenCommand(String id, String email, String refreshToken, Reissue reissue) {
        super(id);
        this.email = email;
        this.refreshToken = refreshToken;
        this.reissue = reissue;
    }
}
