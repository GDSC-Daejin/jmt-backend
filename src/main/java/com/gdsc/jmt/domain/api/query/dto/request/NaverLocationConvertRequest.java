package com.gdsc.jmt.domain.api.query.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record NaverLocationConvertRequest(

        @Schema(description = "경도 위도", example = "127.0596,37.6633")
        String coords
) { }
