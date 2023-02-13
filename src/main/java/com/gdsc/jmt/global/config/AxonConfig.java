package com.gdsc.jmt.global.config;

import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    // TODO: 이거 예제이니까 나중에 새로운 도메인 생기면 이거대로 따라 해주세요 예제는 지워주시고
//    @Bean
//    public EventSourcingRepository< 도메인 Aggregate > accountAggregateEventSourcingRepository(EventStore eventStore) {
//         return EventSourcingRepository.builder(AccountAggregate::class.java).eventStore(eventStore).build();
//    }
}
