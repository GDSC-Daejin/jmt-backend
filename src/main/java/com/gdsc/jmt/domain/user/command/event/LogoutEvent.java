package com.gdsc.jmt.domain.user.command.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LogoutEvent {
    private final String email;
    private final String refreshToken;
}
