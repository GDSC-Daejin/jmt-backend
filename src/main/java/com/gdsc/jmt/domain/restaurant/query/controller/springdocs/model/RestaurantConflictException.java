package com.gdsc.jmt.domain.restaurant.query.controller.springdocs.model;

import com.gdsc.jmt.global.messege.RestaurantMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RestaurantConflictException {
    @Schema(description = "", nullable = true)
    String data = null;

    @Schema(description = "", example = "맛집 위치정보가 이미 등록이 되어있습니다.")
    String message = RestaurantMessage.RESTAURANT_LOCATION_CONFLICT.getMessage();

    @Schema(description = "", example = "RESTAURANT_LOCATION_CONFLICT")
    String code = RestaurantMessage.RESTAURANT_LOCATION_CONFLICT.toString();
}