package com.gdsc.jmt.domain.restaurant.util;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class KaKaoSearchResponse {
    private KaKaoSearchMeta meta;
    private List<KaKaoSearchDocument> documents;
}
