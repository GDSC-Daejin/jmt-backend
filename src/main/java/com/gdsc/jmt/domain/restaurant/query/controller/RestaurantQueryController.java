package com.gdsc.jmt.domain.restaurant.query.controller;

import com.gdsc.jmt.domain.restaurant.query.service.RestaurantQueryService;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@FirstVersionRestController
@RequiredArgsConstructor
public class RestaurantQueryController {

    private final RestaurantQueryService restaurantQueryService;

    @GetMapping("/restaurant/location")
    public JMTApiResponse<?> findRestaurantList() {
        return null;
//        return JMTApiResponse.createResponseWithMessage(null, );
    }
}
