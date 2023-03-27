package com.gdsc.jmt.domain.restaurant.command.event;

import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequest;
import com.gdsc.jmt.global.event.BaseEvent;
import lombok.Getter;

@Getter
public class CreateRecommendRestaurantEvent extends BaseEvent<String> {
    private final CreateRecommendRestaurantRequest createRecommendRestaurantRequest;

    public CreateRecommendRestaurantEvent(String id, CreateRecommendRestaurantRequest createRecommendRestaurantRequest) {
        super(id);
        this.createRecommendRestaurantRequest = createRecommendRestaurantRequest;
    }
}
