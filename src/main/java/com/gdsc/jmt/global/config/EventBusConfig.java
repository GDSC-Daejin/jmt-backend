package com.gdsc.jmt.global.config;

import com.gdsc.jmt.global.interceptor.EventLoggingDispatchInterceptor;
import org.axonframework.eventhandling.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusConfig {
    @Autowired
    public void registerEventInterceptors(EventBus eventBus) {
        eventBus.registerDispatchInterceptor(new EventLoggingDispatchInterceptor());
    }
}
