package com.gdsc.jmt.domain.restaurant.query.dto.request;

public record RestaurantSearchMapRequest(
        MapLocation startLocation,
        MapLocation endLocation,
        RestaurantFilter filter
) { }

