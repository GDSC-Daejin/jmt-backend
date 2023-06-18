package com.gdsc.jmt.domain.restaurant.manager;

import com.gdsc.jmt.domain.restaurant.command.aggregate.RestaurantPhotoAggregate;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRestaurantPhotoEvent;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantPhotoEntity;
import com.gdsc.jmt.domain.restaurant.query.repository.RestaurantPhotoRepository;
import com.gdsc.jmt.global.event.BaseEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantPhotoQueryEntityManager {

    private final EventSourcingRepository<RestaurantPhotoAggregate> restaurantPhotoAggregateEventSourcingRepository;
    private final RestaurantPhotoRepository restaurantPhotoRepository;

    @EventSourcingHandler
    public void createdRestaurantPhoto(CreateRestaurantPhotoEvent event) {
        persistRestaurant(makeEntity(getRestaurantPhotoFromEvent(event)));
    }

    private RestaurantPhotoAggregate getRestaurantPhotoFromEvent(BaseEvent<String> event) {
        return restaurantPhotoAggregateEventSourcingRepository.load(event.getId())
                .getWrappedAggregate()
                .getAggregateRoot();
    }

    private RestaurantPhotoEntity makeEntity(RestaurantPhotoAggregate aggregate) {
        RestaurantPhotoEntity photoEntity = RestaurantPhotoEntity.builder()
                .aggregateId(aggregate.getId())
                .imageUrl(aggregate.getImageUrl())
                .imageSize(aggregate.getImageSize())
                .build();

        return photoEntity;
    }

    private void persistRestaurant(RestaurantPhotoEntity photoEntity) {
        restaurantPhotoRepository.save(photoEntity);
    }
}
