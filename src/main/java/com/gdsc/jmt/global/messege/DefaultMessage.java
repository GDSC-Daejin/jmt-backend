package com.gdsc.jmt.global.messege;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DefaultMessage implements ResponseMessage{
    UNAUTHORIZED("인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("권한이 필요합니다.", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus status;
}
