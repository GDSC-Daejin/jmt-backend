package com.gdsc.jmt.domain.category.query.controller.springdocs.model;

import com.gdsc.jmt.global.messege.CategoryMessage;
import com.gdsc.jmt.global.messege.DefaultMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CategoryNotFoundException {
    @Schema(description = "", nullable = true)
    String data = null;

    @Schema(description = "", example = "해당 카테고리가 존재하지 않습니다.")
    String message = CategoryMessage.CATEGORY_FIND_FAIL.getMessage();

    @Schema(description = "", example = "CATEGORY_FIND_FAIL")
    String code = CategoryMessage.CATEGORY_FIND_FAIL.toString();
}
