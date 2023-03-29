package com.gdsc.jmt.domain.restaurant.command.service;

import com.gdsc.jmt.domain.restaurant.command.CreateRecommendRestaurantCommand;
import com.gdsc.jmt.domain.restaurant.command.CreateRestaurantCommand;
import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.dto.response.CreatedRestaurantResponse;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final CommandGateway commandGateway;

    public CreatedRestaurantResponse createRecommendRestaurant(CreateRecommendRestaurantRequest createRecommendRestaurantRequest) {
        // TODO : 이미지 정보 등록 기능 필요
        String aggregateId = UUID.randomUUID().toString();
        commandGateway.sendAndWait(new CreateRecommendRestaurantCommand(
                aggregateId,
                createRecommendRestaurantRequest
        ));

        return new CreatedRestaurantResponse(createRecommendRestaurantRequest.getKakaoSubId(), aggregateId);
    }

    public String createRestaurantLocation(KakaoSearchDocument kakaoSearchDocumentRequest) {
        String aggregateId = UUID.randomUUID().toString();
        commandGateway.sendAndWait(new CreateRestaurantCommand(
                aggregateId,
                kakaoSearchDocumentRequest
        ));

        return aggregateId;
    }
}
