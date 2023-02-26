package com.gdsc.jmt.global.messege;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserMessage implements ResponseMessage{

    LOGIN_SUCCESS("로그인에 성공했습니다.", HttpStatus.OK);



    private final String message;
    private final HttpStatus status;
}
