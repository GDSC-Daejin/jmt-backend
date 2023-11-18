package com.gdsc.jmt.domain.restaurant.query.dto.request;

public record RestaurantSearchRequest(
    String keyword,
    MapLocation userLocation
) { }
