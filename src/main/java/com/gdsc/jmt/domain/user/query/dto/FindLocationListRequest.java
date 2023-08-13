package com.gdsc.jmt.domain.user.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindLocationListRequest(
        @Schema(description = "검색할 맛집 이름", example = "마제소바")
        String query,

        @Schema(description = "페이지 정보", example = "1")
        Integer page
) {
}
