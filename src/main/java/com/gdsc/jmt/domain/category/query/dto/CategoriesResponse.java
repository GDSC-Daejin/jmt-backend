package com.gdsc.jmt.domain.category.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CategoriesResponse(
        @Schema(description = "카테고리 리스트 정보입니다.")
        List<CategoryResponse> categories
) { }
