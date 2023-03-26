package com.gdsc.jmt.domain.restaurant.util;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoSearchResponse {
    private KakaoSearchMeta meta;
    private List<KakaoSearchDocument> documents;
}
