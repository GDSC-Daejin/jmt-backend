package com.gdsc.jmt.global.messege;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultMessage implements ResponseMessage{
    UNAUTHORIZED("인증이 필요합니다.", 401),
    FORBIDDEN("권한이 필요합니다.", 403);

    private final String message;
    private final Integer statusCode;
}
