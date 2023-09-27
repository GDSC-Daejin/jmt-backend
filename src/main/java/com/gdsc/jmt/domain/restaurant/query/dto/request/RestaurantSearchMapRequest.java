package com.gdsc.jmt.domain.restaurant.query.dto.request;

public record RestaurantSearchMapRequest(
        MapLocation userLocation,
        MapLocation startLocation,
        MapLocation endLocation,
        RestaurantFilter filter
) { }

