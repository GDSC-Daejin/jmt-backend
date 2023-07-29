package com.gdsc.jmt.domain.restaurant.query.dto.request;

public record RestaurantSearchMapRequest(
        String x,   // 사용자 경도
        String y,   // 사용자 위도
        Integer radius, // meter 단위
        RestaurantFilter filter
) { }
