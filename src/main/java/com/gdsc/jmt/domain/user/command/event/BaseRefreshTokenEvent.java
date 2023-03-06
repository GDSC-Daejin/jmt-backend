package com.gdsc.jmt.domain.user.command.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class BaseRefreshTokenEvent<T> {
    protected final T id;
}
