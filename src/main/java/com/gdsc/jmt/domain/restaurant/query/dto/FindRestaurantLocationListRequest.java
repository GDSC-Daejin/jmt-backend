package com.gdsc.jmt.domain.restaurant.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindRestaurantLocationListRequest(
        @Schema(description = "검색할 맛집 이름", example = "마제소바")
        String query,

        @Schema(description = "페이지 정보", example = "1")
        Integer page,

        @Schema(description = "사용자 위치 경도")
        String x,

        @Schema(description = "사용자 위치 위도")
        String y
) { }
