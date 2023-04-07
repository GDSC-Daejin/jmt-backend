package com.gdsc.jmt.domain.restaurant.query.controller.springdocs.model;

import com.gdsc.jmt.global.messege.RestaurantMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RestaurantNotFoundException {
    @Schema(description = "", nullable = true)
    String data = null;

    @Schema(description = "", example = "맛집 위치정보가 등록되지 않았습니다.")
    String message = RestaurantMessage.RESTAURANT_LOCATION_NOT_FOUND.getMessage();

    @Schema(description = "", example = "RESTAURANT_LOCATION_NOT_FOUND")
    String code = RestaurantMessage.RESTAURANT_LOCATION_NOT_FOUND.toString();
}
