package com.gdsc.jmt.domain.category.query.controller.springdocs;


import com.gdsc.jmt.domain.user.command.controller.springdocs.model.AuthInvalidTokenException;
import com.gdsc.jmt.domain.user.command.controller.springdocs.model.ServerErrorException;
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
@Operation(summary = "카테고리 전체 조회 API", description = "모든 카테고리에 대해서 조회합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
})
public @interface CategoriesSpringDocs {
}
