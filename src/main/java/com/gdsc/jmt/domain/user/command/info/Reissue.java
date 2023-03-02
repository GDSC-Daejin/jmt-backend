package com.gdsc.jmt.domain.user.command.info;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Reissue {
    private final boolean isReissue;
    private final String oldRefreshToken;
    private final String newRefreshToken;
}
