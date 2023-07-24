package com.gdsc.jmt.domain.restaurant.query.dto;

public record RestaurantSearchMapRequest(
        String x,   // 사용자 경도
        String y,   // 사용자 위도
        Integer radius, // meter 단위
        String categoryFilter,  // CAFE, FUSION, KOREAN 이런식의 형태
        boolean isCanDrinkLiquor    // 술 마실 수 있는 여부
) { }
