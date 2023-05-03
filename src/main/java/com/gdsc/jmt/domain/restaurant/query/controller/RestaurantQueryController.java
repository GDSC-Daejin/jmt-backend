package com.gdsc.jmt.domain.restaurant.query.controller;

import com.gdsc.jmt.domain.restaurant.query.controller.springdocs.CheckRecommendRestaurantExistingSpringDocs;
import com.gdsc.jmt.domain.restaurant.query.controller.springdocs.FindRestaurantLocationSpringDocs;
import com.gdsc.jmt.domain.restaurant.query.dto.FindRestaurantLocationListRequest;
import com.gdsc.jmt.domain.restaurant.query.dto.response.FindDetailRestaurantResponse;
import com.gdsc.jmt.domain.restaurant.query.service.RestaurantQueryService;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.domain.user.command.controller.springdocs.FindDetailRestaurantSpringDocs;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FirstVersionRestController
@RequiredArgsConstructor
@Tag(name = "맛집 조회 컨트롤러")
public class RestaurantQueryController {
    private final RestaurantQueryService restaurantQueryService;

    @GetMapping("/restaurant/location")
    @FindRestaurantLocationSpringDocs
    public JMTApiResponse<List<KakaoSearchDocument>> findRestaurantLocationList(@ModelAttribute FindRestaurantLocationListRequest findRestaurantLocationListRequest) {
        List<KakaoSearchDocument> restaurants = restaurantQueryService.findRestaurantLocationList(findRestaurantLocationListRequest);
        return JMTApiResponse.createResponseWithMessage(restaurants, RestaurantMessage.RESTAURANT_LOCATION_FIND);
    }

    @GetMapping("/restaurant/registration/{kakaoSubId}")
    @CheckRecommendRestaurantExistingSpringDocs
    public JMTApiResponse<?> checkRecommendRestaurantExisting(@PathVariable String kakaoSubId) {
        restaurantQueryService.checkRecommendRestaurantExisting(kakaoSubId);
        return JMTApiResponse.createResponseWithMessage(null, RestaurantMessage.RECOMMEND_RESTAURANT_REGISTERABLE);
    }

    @GetMapping("restaurant/{recommendRestaurantId}")
    @FindDetailRestaurantSpringDocs
    public JMTApiResponse<?> getDetailRestaurant(@PathVariable Long recommendRestaurantId) {
        FindDetailRestaurantResponse response = restaurantQueryService.findDetailRestaurant(recommendRestaurantId);
        return JMTApiResponse.createResponseWithMessage(response, RestaurantMessage.DETAIL_RESTAURANT_FIND_SUCCESS);
    }

}
