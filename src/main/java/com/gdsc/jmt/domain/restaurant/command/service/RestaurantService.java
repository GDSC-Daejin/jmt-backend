package com.gdsc.jmt.domain.restaurant.command.service;

import com.gdsc.jmt.domain.restaurant.command.CreateRecommendRestaurantCommand;
import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.dto.request.RecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.dto.response.CreatedRestaurantResponse;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final CommandGateway commandGateway;

    // TODO : 네이버 API 연동 전 로직
    public CreatedRestaurantResponse createRestaurant(CreateRestaurantRequest createRestaurantRequest) {
        String testName = createRestaurantRequest.getName();
//        createRestaurantInfo();

        RecommendRestaurantRequest recommendRestaurantRequest = RecommendRestaurantRequest.builder()
                .introduce(createRestaurantRequest.getIntroduce())
                .categoryId(createRestaurantRequest.getCategoryId())
//                .pictures(createRestaurantRequest.getPictures())
                .canDrinkLiquor(createRestaurantRequest.getCanDrinkLiquor())
                .goWellWithLiquor(createRestaurantRequest.getGoWellWithLiquor())
                .recommendMenu(createRestaurantRequest.getRecommendMenu())
                .build();

        String recommendRestaurantAggregateId = createRecommendRestaurant(
                recommendRestaurantRequest, testName);
        return new CreatedRestaurantResponse("test", recommendRestaurantAggregateId);
    }

    private void createRestaurantInfo() {
        // 기본 발급
//        commandGateway.send();
    }

    private String createRecommendRestaurant(RecommendRestaurantRequest recommendRestaurantRequest, String restaurantName) {
        String aggregateId = UUID.randomUUID().toString();
        commandGateway.sendAndWait(new CreateRecommendRestaurantCommand(
                aggregateId,
                recommendRestaurantRequest,
                restaurantName
        ));

        return aggregateId;
    }
}
