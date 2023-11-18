package com.gdsc.jmt.domain.restaurant.query.dto.request;

public record RestaurantSearchInUserIdRequest(
        MapLocation userLocation,
        RestaurantFilter filter
) { }
