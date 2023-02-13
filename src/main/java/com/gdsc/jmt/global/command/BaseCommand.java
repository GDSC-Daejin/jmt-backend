package com.gdsc.jmt.global.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class BaseCommand<T> {
    @TargetAggregateIdentifier
    T id;
}
