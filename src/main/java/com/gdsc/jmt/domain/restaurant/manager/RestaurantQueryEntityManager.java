package com.gdsc.jmt.domain.restaurant.manager;

import com.gdsc.jmt.domain.restaurant.command.aggregate.RestaurantAggregate;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRestaurantEvent;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.repository.RestaurantRepository;
import com.gdsc.jmt.global.event.BaseEvent;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestaurantQueryEntityManager {
    private final EventSourcingRepository<RestaurantAggregate> restaurantAggregateEventSourcingRepository;

    private final RestaurantRepository restaurantRepository;

    @EventSourcingHandler
    public void createdRestaurant(CreateRestaurantEvent event) {
        Optional<RestaurantEntity> checkExisting = checkExisting(event.getKakaoSearchDocument().getId());
        if(checkExisting.isPresent()) {
            throw new ApiException(RestaurantMessage.RESTAURANT_LOCATION_CONFLICT);
        }
        persistRestaurant(makeRestaurant(getRestaurantFromEvent(event)));
    }

    private RestaurantAggregate getRestaurantFromEvent(BaseEvent<String> event) {
        return restaurantAggregateEventSourcingRepository.load(event.getId())
                .getWrappedAggregate()
                .getAggregateRoot();
    }

    private Optional<RestaurantEntity> checkExisting(String kakaoSubId) {
        return restaurantRepository.findByKakaoSubId(kakaoSubId);
    }

    private RestaurantEntity makeRestaurant(RestaurantAggregate restaurantAggregate) {
        return RestaurantEntity.builder()
                .kakaoSubId(restaurantAggregate.getKakaoSubId())
                .name(restaurantAggregate.getName())
                .placeUrl(restaurantAggregate.getPlaceUrl())
                .category(restaurantAggregate.getCategory())
                .phone(restaurantAggregate.getPhone())
                .address(restaurantAggregate.getAddress())
                .roadAddress(restaurantAggregate.getRoadAddress())
                .location(restaurantAggregate.getLocation())
                .aggregateId(restaurantAggregate.getId())
                .build();
    }

    private void persistRestaurant(RestaurantEntity restaurant) {
        restaurantRepository.save(restaurant);
    }
}
