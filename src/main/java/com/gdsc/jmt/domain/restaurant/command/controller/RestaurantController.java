package com.gdsc.jmt.domain.restaurant.command.controller;

import com.gdsc.jmt.domain.restaurant.command.controller.springdocs.CreateRecommendRestaurantSpringDocs;
import com.gdsc.jmt.domain.restaurant.command.controller.springdocs.CreateRestaurantLocationSpringDocs;
import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.dto.response.CreatedRestaurantResponse;
import com.gdsc.jmt.domain.restaurant.command.service.RestaurantService;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FirstVersionRestController
@RequiredArgsConstructor
@Tag(name = "맛집 등록 컨트롤러")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping(value = "/restaurant/location")
    @CreateRestaurantLocationSpringDocs
    public JMTApiResponse<String> createRestaurantLocation(@RequestBody KakaoSearchDocument kakaoSearchDocumentRequest) {
        String aggregateId = restaurantService.createRestaurantLocation(kakaoSearchDocumentRequest);
        return JMTApiResponse.createResponseWithMessage(aggregateId, RestaurantMessage.RESTAURANT_LOCATION_CREATED);
    }

    @PostMapping(value = "/restaurant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CreateRecommendRestaurantSpringDocs
    public JMTApiResponse<?> createRecommendRestaurant(@ModelAttribute CreateRecommendRestaurantRequest createRecommendRestaurantRequest) {
        CreatedRestaurantResponse response = restaurantService.createRecommendRestaurant(createRecommendRestaurantRequest);
        return JMTApiResponse.createResponseWithMessage(response, RestaurantMessage.RESTAURANT_CREATED);
    }
}
