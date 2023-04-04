package com.gdsc.jmt.domain.restaurant.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KakaoSearchResponse {
    private KakaoSearchMeta meta;
    private List<KakaoSearchDocument> documents;
}
