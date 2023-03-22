package com.gdsc.jmt.domain.restaurant.command.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


// TODO : 네이버 API 연동 전 로직
@Getter
@Setter
public class CreateRestaurantInfoRequest {
        @Schema(description = "임시 식당 이름", example = "마라탕")
        private String name;
}
