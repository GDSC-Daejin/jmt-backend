package com.gdsc.jmt.domain.restaurant.query.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record MapLocation(
        @Schema(description = "경도", example = "127.0596")
        String x,
        @Schema(description = "위도", example = "37.6633")
        String y
) { }
