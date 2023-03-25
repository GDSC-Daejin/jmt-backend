package com.gdsc.jmt.domain.restaurant.query.controller;

import com.gdsc.jmt.domain.restaurant.query.service.RestaurantQueryService;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FirstVersionRestController
@RequiredArgsConstructor
public class RestaurantQueryController {
    private final RestaurantQueryService restaurantQueryService;

    @GetMapping("/restaurant/location")
    public JMTApiResponse<List<KakaoSearchDocument>> findRestaurantList(@RequestParam String query, @RequestParam Integer page) {
        List<KakaoSearchDocument> restaurants = restaurantQueryService.findRestaurantLocationList(query, page);
        return JMTApiResponse.createResponseWithMessage(restaurants, RestaurantMessage.RESTAURANT_LOCATION_FIND);
    }
}
