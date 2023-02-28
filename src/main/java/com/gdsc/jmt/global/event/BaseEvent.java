package com.gdsc.jmt.global.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class BaseEvent<T> {
    protected final T id;
}
