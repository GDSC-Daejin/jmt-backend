package com.gdsc.jmt.domain.restaurant.command.event;

import com.gdsc.jmt.domain.restaurant.command.dto.request.RecommendRestaurantRequest;
import com.gdsc.jmt.global.event.BaseEvent;
import lombok.Getter;

@Getter
public class CreateRecommendRestaurantEvent extends BaseEvent<String> {
    // TODO : 네이버 API 연동전 로직
    private final RecommendRestaurantRequest recommendRestaurantRequest;
    private final String restaurantName;
    public CreateRecommendRestaurantEvent(String id, RecommendRestaurantRequest recommendRestaurantRequest, String restaurantName) {
        super(id);
        this.recommendRestaurantRequest = recommendRestaurantRequest;
        this.restaurantName = restaurantName;
    }
}
