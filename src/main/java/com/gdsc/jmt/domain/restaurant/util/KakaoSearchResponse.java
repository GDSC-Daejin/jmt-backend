package com.gdsc.jmt.domain.restaurant.util;

import com.gdsc.jmt.global.util.kakao.KakaoSearchMeta;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KakaoSearchResponse {
    private KakaoSearchMeta meta;
    private List<KakaoSearchDocument> documents;
}
