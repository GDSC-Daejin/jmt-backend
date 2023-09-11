package com.gdsc.jmt.domain.user.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoSearchDocumentResponse {
    @Schema(description = "이름")
    private String placeName;

    @Schema(description = "주소", example = "대구 동구 신천동 384-12")
    private String addressName;

    @Schema(description = "도로명 주소", example = "대구 동구 동부로 158-7")
    private String roadAddressName;

    @Schema(description = "위도", example = "128.629555401333")
    private String x;

    @Schema(description = "경도", example = "35.8768967936303")
    private String y;
}
