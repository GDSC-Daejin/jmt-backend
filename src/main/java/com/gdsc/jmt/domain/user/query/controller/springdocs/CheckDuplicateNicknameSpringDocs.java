package com.gdsc.jmt.domain.user.query.controller.springdocs;

import com.gdsc.jmt.global.controller.springdocs.ServerErrorException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Operation(summary = "닉네임 중복 확인 API", description = "닉네임을 등록하기 전에 등록 가능한 닉네임인지 확인하는 API 입니다.")
@Parameter(name = "nickname", description = "닉네임", required = true)
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
        @ApiResponse(
                responseCode = "409",
                description = "이미 존재하는 닉네임 입니다.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = NicknameConflictErrorException.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "알수 없는 서버 에러 발생",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServerErrorException.class))
        )

})

public @interface CheckDuplicateNicknameSpringDocs {
}
