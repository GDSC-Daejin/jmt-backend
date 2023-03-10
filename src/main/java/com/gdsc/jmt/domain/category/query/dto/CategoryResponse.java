package com.gdsc.jmt.domain.category.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponse(
        @Schema(description = "카테고리 Id 입니다.", example = "1")
        Long id,
        @Schema(description = "카테고리 이름입니다.", example = "양식")
        String name
) { }
