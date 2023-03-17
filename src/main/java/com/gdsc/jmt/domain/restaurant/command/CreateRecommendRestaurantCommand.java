package com.gdsc.jmt.domain.restaurant.command;

import com.gdsc.jmt.domain.restaurant.command.dto.RecommendRestaurantRequest;
import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class CreateRecommendRestaurantCommand extends BaseCommand<String> {
    private final RecommendRestaurantRequest recommendRestaurantRequest;

    public CreateRecommendRestaurantCommand(String id, RecommendRestaurantRequest recommendRestaurantRequest) {
        super(id);
        this.recommendRestaurantRequest = recommendRestaurantRequest;
    }
}
