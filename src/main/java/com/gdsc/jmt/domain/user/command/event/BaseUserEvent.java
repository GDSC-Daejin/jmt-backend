package com.gdsc.jmt.domain.user.command.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class BaseUserEvent<T> {
    protected final T id;
}
