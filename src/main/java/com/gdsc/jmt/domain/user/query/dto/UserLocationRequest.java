package com.gdsc.jmt.domain.user.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLocationRequest (
        @Schema(description = "경도", example = "127.0596")
        String x,
        @Schema(description = "위도", example = "37.6633")
        String y
){ }
