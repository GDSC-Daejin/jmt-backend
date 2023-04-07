package com.gdsc.jmt.domain.restaurant.query.controller.springdocs;

import com.gdsc.jmt.domain.restaurant.query.controller.springdocs.model.RestaurantConflictException;
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
@Operation(summary = "맛집 위치정보 등록 여부 조회 API", description = "맛집 위치정보가 등록되었는지 여부를 조회합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "404", useReturnTypeSchema = true),
        @ApiResponse(
                responseCode = "409",
                description = "맛집 위치정보가 이미 등록이 되어있습니다.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantConflictException.class))
        )
})
public @interface CheckRestaurantExistingSpringDocs { }
