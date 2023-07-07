package com.gdsc.jmt.domain.restaurant.command.dto.response;

public record CreatedRestaurantResponse(
        Long restaurantLocationId,
        Long recommendRestaurantId
) { }
