package com.gdsc.jmt.domain.restaurant.query.dto.request;

public record FindMyReviewRequest(
        MapLocation userLocation,
        RestaurantFilter filter
) { }
