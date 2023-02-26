package com.gdsc.jmt.global.config;

import com.gdsc.jmt.domain.user.command.aggregate.UserAggregate;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    @Bean
    public EventSourcingRepository<UserAggregate> userAggregateEventSourcingRepository(EventStore eventStore) {
        return EventSourcingRepository.builder(UserAggregate.class).eventStore(eventStore).build();
    }
}
