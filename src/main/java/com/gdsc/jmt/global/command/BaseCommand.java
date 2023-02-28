package com.gdsc.jmt.global.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@RequiredArgsConstructor
public abstract class BaseCommand<T> {
    @TargetAggregateIdentifier
    protected final T id;
}
