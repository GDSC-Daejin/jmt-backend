package com.gdsc.jmt.domain.category.query.controller.springdocs;

import com.gdsc.jmt.domain.category.query.controller.springdocs.model.CategoryNotFoundException;
import com.gdsc.jmt.domain.user.command.controller.springdocs.model.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "특정 카테고리 조회 API", description = "특정 카테고리에 대해 조회합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "404", description = "해당 카테고리를 찾을 수 없습니다.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryNotFoundException.class)))})
public @interface CategorySpringDocs {
}
