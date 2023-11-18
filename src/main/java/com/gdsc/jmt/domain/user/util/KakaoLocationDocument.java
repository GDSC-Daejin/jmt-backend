package com.gdsc.jmt.domain.user.util;

import com.gdsc.jmt.domain.user.util.dto.Address;
import com.gdsc.jmt.domain.user.util.dto.RoadAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoLocationDocument {
    @Schema(description = "주소", example = "대구 동구 신천동 384-12")
    private Address address;

    @Schema(description = "도로명 주소", example = "대구 동구 동부로 158-7")
    private RoadAddress road_address;
}