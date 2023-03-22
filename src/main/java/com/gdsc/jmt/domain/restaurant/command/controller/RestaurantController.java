package com.gdsc.jmt.domain.restaurant.command.controller;

import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.dto.response.CreatedRestaurantResponse;
import com.gdsc.jmt.domain.restaurant.command.service.RestaurantService;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FirstVersionRestController
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    // TODO : 네이버 API 연동 전, Security 적용 전 로직
    @PostMapping(value = "/restaurant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JMTApiResponse<?> createRestaurant(@ModelAttribute CreateRestaurantRequest createRestaurantRequest) {
        CreatedRestaurantResponse response = restaurantService.createRestaurant(createRestaurantRequest);
        return JMTApiResponse.createResponseWithMessage(null, RestaurantMessage.RESTAURANT_CREATED);
    }
}
