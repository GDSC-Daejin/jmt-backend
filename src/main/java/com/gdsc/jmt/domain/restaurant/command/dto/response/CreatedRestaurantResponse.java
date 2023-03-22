package com.gdsc.jmt.domain.restaurant.command.dto.response;

public record CreatedRestaurantResponse(
        String restaurantAggregateId,
        String recommendRestaurantAggregateId
) { }
