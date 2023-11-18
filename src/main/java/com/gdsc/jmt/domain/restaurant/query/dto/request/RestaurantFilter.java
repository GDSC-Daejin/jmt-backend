package com.gdsc.jmt.domain.restaurant.query.dto.request;

public record RestaurantFilter(
        String categoryFilter,  // CAFE, FUSION, KOREAN 이런식의 형태
        Boolean isCanDrinkLiquor    // 술 마실 수 있는 여부
) { }
