package com.gdsc.jmt.domain.user.command.event;

import com.gdsc.jmt.domain.user.command.info.Reissue;
import lombok.Getter;

@Getter
public class PersistRefreshTokenEvent extends BaseRefreshTokenEvent<String> {

    private final String email;
    private final String refreshToken;
    private final Reissue reissue;

    public PersistRefreshTokenEvent(String id, String email, String refreshToken, Reissue reissue) {
        super(id);
        this.email = email;
        this.refreshToken = refreshToken;
        this.reissue = reissue;
    }
}
