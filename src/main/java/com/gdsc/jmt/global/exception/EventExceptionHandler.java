package com.gdsc.jmt.global.exception;

import com.gdsc.jmt.global.interceptor.EventLoggingDispatchInterceptor;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;

public class EventExceptionHandler implements ListenerInvocationErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(EventLoggingDispatchInterceptor.class);
    @Override
    public void onError(@Nonnull Exception exception, @Nonnull EventMessage<?> event, @Nonnull EventMessageHandler eventHandler) throws Exception {
        logger.error("Error is occurred from : " + event);
        if(exception instanceof ApiException)
            logger.error("Error Reason : " + ((ApiException) exception).getResponseMessage().getMessage());
        else
            logger.error("Error Reason : " + exception.getMessage());
    }
}
