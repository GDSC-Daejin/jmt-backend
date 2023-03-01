package com.gdsc.jmt.global.messege;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserMessage implements ResponseMessage{
    USER_NOT_FOUND("해당 유저는 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    LOGIN_CONFLICT("이미 로그인이 되어있습니다.", HttpStatus.CONFLICT),
    LOGIN_SUCCESS("로그인에 성공했습니다.", HttpStatus.OK),
    LOGOUT_SUCCESS("로그아웃에 성공했습니다.", HttpStatus.OK),
    LOGOUT_FAIL("RefreshToken이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;
}
