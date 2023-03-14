package com.gdsc.jmt.global.logs.interceptor;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

public class EventLoggingDispatchInterceptor implements MessageDispatchInterceptor<EventMessage<?>> {
    private static final Logger logger = LoggerFactory.getLogger(EventLoggingDispatchInterceptor.class);

    // Event Dispatch 인터셉터
    @Nonnull
    @Override
    public BiFunction<Integer, EventMessage<?>, EventMessage<?>> handle(
            @Nonnull List<? extends EventMessage<?>> messages) {
        return (index, event) -> {
            logger.info("Publishing event: [{}].", event);
            return event;
        };
    }
}
