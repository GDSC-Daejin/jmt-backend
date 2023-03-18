package com.gdsc.jmt.domain.restaurant.command.event;

import com.gdsc.jmt.domain.restaurant.command.dto.RecommendRestaurantRequest;
import com.gdsc.jmt.global.event.BaseEvent;
import lombok.Getter;

@Getter
public class CreateRecommendRestaurantEvent extends BaseEvent<String> {
    private final RecommendRestaurantRequest recommendRestaurantRequest;
    public CreateRecommendRestaurantEvent(String id, RecommendRestaurantRequest recommendRestaurantRequest) {
        super(id);
        this.recommendRestaurantRequest = recommendRestaurantRequest;
    }
}
