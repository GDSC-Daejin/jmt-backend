package com.gdsc.jmt.domain.restaurant.query.dto.request;

public record RestaurantSearchFromOtherGroupRequest(
        String keyword,
        Long currentGroupId
) { }
