package com.gdsc.jmt.global.messege;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RestaurantMessage implements ResponseMessage {
    RESTAURANT_CREATED("맛집이 등록되었습니다." , HttpStatus.CREATED),

    RESTAURANT_LOCATION_FIND("맛집 위치 정보를 조회하였습니다." , HttpStatus.OK);

    private final String message;
    private final HttpStatus status;
}
