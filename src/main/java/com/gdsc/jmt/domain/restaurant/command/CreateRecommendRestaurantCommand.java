package com.gdsc.jmt.domain.restaurant.command;

import com.gdsc.jmt.domain.restaurant.command.dto.request.RecommendRestaurantRequest;
import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class CreateRecommendRestaurantCommand extends BaseCommand<String> {
    // TODO : 네이버 API 연동 이전 로직
    private final RecommendRestaurantRequest recommendRestaurantRequest;

    private final String restaurantName;

    public CreateRecommendRestaurantCommand(String id, RecommendRestaurantRequest recommendRestaurantRequest, String restaurantName) {
        super(id);
        this.recommendRestaurantRequest = recommendRestaurantRequest;
        this.restaurantName = restaurantName;
    }
}
