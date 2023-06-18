package com.gdsc.jmt.domain.restaurant.command.aggregate;

import com.gdsc.jmt.domain.restaurant.command.CreateRecommendRestaurantCommand;
import com.gdsc.jmt.domain.restaurant.command.CreateRestaurantPhotoCommand;
import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRecommendRestaurantEvent;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRestaurantPhotoEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Getter
@RequiredArgsConstructor
public class RestaurantPhotoAggregate {
    @AggregateIdentifier
    private String id;

    private String imageUrl;
    private Long imageSize;

    @CommandHandler
    public RestaurantPhotoAggregate(CreateRestaurantPhotoCommand command) {
        AggregateLifecycle.apply(new CreateRestaurantPhotoEvent(
                command.getId(),
                command.getImageUrl(),
                command.getImageSize()
        ));
    }

    @EventSourcingHandler
    public void createdRestaurantPhoto(CreateRestaurantPhotoEvent event) {
        this.id = event.getId();
        this.imageUrl = event.getImageUrl();
        this.imageSize = event.getImageSize();
    }

}
