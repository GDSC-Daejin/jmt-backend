package com.gdsc.jmt.domain.restaurant.query.controller.springdocs;

import com.gdsc.jmt.domain.restaurant.query.controller.springdocs.model.RecommendRestaurantConflictException;
import com.gdsc.jmt.domain.restaurant.query.controller.springdocs.model.RestaurantNotFoundException;
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
@Operation(summary = "맛집 등록 여부 조회 API", description = "맛집이 등록되었는지 여부를 조회합니다.\n" +
        "200 상태코드 이면 맛집을 등록할 수 있다는 뜻입니다.\n" +
        "409 상태코드 이면 맛집이 이미 등록이 되어있다는 뜻입니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
        @ApiResponse(
                responseCode = "409",
                description = "맛집이 이미 등록이 되어있습니다.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecommendRestaurantConflictException.class))
        )
})
public @interface CheckRecommendRestaurantExistingSpringDocs { }
