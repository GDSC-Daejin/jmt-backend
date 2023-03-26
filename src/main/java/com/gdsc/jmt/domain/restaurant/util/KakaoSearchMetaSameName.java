package com.gdsc.jmt.domain.restaurant.util;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoSearchMetaSameName {
    private List<String> region;

    private String keyword;
    private String selected_region;
}
