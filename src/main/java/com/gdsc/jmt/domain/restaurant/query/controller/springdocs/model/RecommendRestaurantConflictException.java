package com.gdsc.jmt.domain.restaurant.query.controller.springdocs.model;

import com.gdsc.jmt.global.messege.RestaurantMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RecommendRestaurantConflictException {
    @Schema(description = "", nullable = true)
    String data = null;

    @Schema(description = "", example = "해당 맛집을 찾을 수 없습니다.")
    String message = RestaurantMessage.RESTAURANT_CONFLICT.getMessage();

    @Schema(description = "", example = "RESTAURANT_CONFLICT")
    String code = RestaurantMessage.RESTAURANT_CONFLICT.toString();
}