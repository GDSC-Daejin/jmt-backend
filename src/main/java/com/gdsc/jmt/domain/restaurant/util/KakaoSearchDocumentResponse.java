package com.gdsc.jmt.domain.restaurant.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoSearchDocumentResponse {

    @Schema(description = "식당 이름", example = "마제소바")
    private String placeName;

    @Schema(description = "거리", example = "0")
    private String distance;

    @Schema(description = "식당 링크", example = "http://place.map.kakao.com/1574464357")
    private String placeUrl;

    @Schema(description = "식당 카테고리", example = "음식점 > 일식")
    private String categoryName;

    @Schema(description = "식당 주소", example = "대구 동구 신천동 384-12")
    private String addressName;

    @Schema(description = "식당 도로명 주소", example = "대구 동구 동부로 158-7")
    private String roadAddressName;

    @Schema(description = "식당 Kakao Sub ID", example = "1574464357")
    private String id;

    @Schema(description = "식당 전화번호", example = "")
    private String phone;

    @Schema(description = "식당 카테고리 코드", example = "FD6")
    private String categoryGroupCode;

    @Schema(description = "식당 카테고리 이름", example = "음식점")
    private String categoryGroupName;

    @Schema(description = "식당 위도", example = "128.629555401333")
    private String x;

    @Schema(description = "식당 경도", example = "35.8768967936303")
    private String y;
}
