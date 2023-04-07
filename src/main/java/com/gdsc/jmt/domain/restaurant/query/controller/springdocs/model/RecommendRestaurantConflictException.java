package com.gdsc.jmt.domain.restaurant.query.controller.springdocs.model;

import com.gdsc.jmt.global.messege.RestaurantMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RecommendRestaurantConflictException {
    @Schema(description = "", nullable = true)
    String data = null;

    @Schema(description = "", example = "맛집이 이미 등록이 되어있습니다.")
    String message = RestaurantMessage.RECOMMEND_RESTAURANT_CONFLICT.getMessage();

    @Schema(description = "", example = "RECOMMEND_RESTAURANT_CONFLICT")
    String code = RestaurantMessage.RECOMMEND_RESTAURANT_CONFLICT.toString();
}