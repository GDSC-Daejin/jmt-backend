package com.gdsc.jmt.global.messege;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GroupMessage implements ResponseMessage{
    CREATED_GROUP("그룹을 생성하였습니다." , HttpStatus.OK),
    FIND_GROUP("그룹 조회에 성공하였습니다." , HttpStatus.OK),
    GROUP_NOT_FOUND("해당 그룹은 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    private final String message;
    private final HttpStatus status;

}
