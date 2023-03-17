package com.gdsc.jmt.domain.restaurant.command.aggregate;

import com.gdsc.jmt.domain.restaurant.command.CreateRestaurantCommand;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRestaurantEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@RequiredArgsConstructor
public class RestaurantAggregate {
    @AggregateIdentifier
    private String id;

    private String name;
    private String address;

    private Double latitude;   // 위도
    private Double longitude;  // 경도

    @CommandHandler
    public RestaurantAggregate(CreateRestaurantCommand createRestaurantCommand) {
        AggregateLifecycle.apply(new CreateRestaurantEvent(
                createRestaurantCommand.getId(),
                createRestaurantCommand.getName(),
                createRestaurantCommand.getAddress(),
                createRestaurantCommand.getLatitude(),
                createRestaurantCommand.getLongitude()
        ));
    }

    @EventSourcingHandler
    public void on(CreateRestaurantEvent createRestaurantEvent) {
        this.id = createRestaurantEvent.getId();
        this.name = createRestaurantEvent.getName();
        this.address = createRestaurantEvent.getAddress();
        this.latitude = createRestaurantEvent.getLatitude();
        this.longitude = createRestaurantEvent.getLongitude();
    }
}
