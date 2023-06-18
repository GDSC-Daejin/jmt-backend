package com.gdsc.jmt.domain.restaurant.command;

import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequest;
import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class CreateRecommendRestaurantCommand extends BaseCommand<String> {
    // TODO : 해당 List<MultipartFile> 필요없음 추후 다른 객체 필요.
    private final CreateRecommendRestaurantRequest createRecommendRestaurantRequest;
    private final String userAggregateId;

    public CreateRecommendRestaurantCommand(String id, CreateRecommendRestaurantRequest createRecommendRestaurantRequest, String userAggregateId) {
        super(id);
        this.createRecommendRestaurantRequest = createRecommendRestaurantRequest;
        this.userAggregateId = userAggregateId;
    }
}
