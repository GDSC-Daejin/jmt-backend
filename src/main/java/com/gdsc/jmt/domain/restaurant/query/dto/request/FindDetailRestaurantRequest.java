package com.gdsc.jmt.domain.restaurant.query.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FindDetailRestaurantRequest {
    @Schema(description = "경도", example = "127.0596")
    private String x;
    @Schema(description = "위도", example = "37.6633")
    private String y;
}
