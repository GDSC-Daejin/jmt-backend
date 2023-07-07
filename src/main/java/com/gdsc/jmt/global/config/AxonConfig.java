package com.gdsc.jmt.global.config;

import com.gdsc.jmt.domain.restaurant.command.aggregate.RecommendRestaurantAggregate;
import com.gdsc.jmt.domain.restaurant.command.aggregate.RestaurantAggregate;
import com.gdsc.jmt.domain.restaurant.command.aggregate.RestaurantPhotoAggregate;
import com.gdsc.jmt.global.exception.EventExceptionHandler;
import org.axonframework.config.ConfigurerModule;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    public EventSourcingRepository<RecommendRestaurantAggregate> recommendRestaurantAggregateEventSourcingRepository(EventStore eventStore) {
        return EventSourcingRepository.builder(RecommendRestaurantAggregate.class).eventStore(eventStore).build();
    }

    @Bean
    public EventSourcingRepository<RestaurantAggregate> restaurantAggregateEventSourcingRepository(EventStore eventStore) {
        return EventSourcingRepository.builder(RestaurantAggregate.class).eventStore(eventStore).build();
    }

    @Bean
    public EventSourcingRepository<RestaurantPhotoAggregate> restaurantPhotoAggregateEventSourcingRepository(EventStore eventStore) {
        return EventSourcingRepository.builder(RestaurantPhotoAggregate.class).eventStore(eventStore).build();
    }

    @Bean
    public ConfigurerModule processingGroupErrorHandlingConfigurerModule() {
        return configurer -> configurer.eventProcessing(
                processingConfigurer -> processingConfigurer.registerDefaultListenerInvocationErrorHandler(
                        conf -> new EventExceptionHandler()
                )
        );
    }
}
