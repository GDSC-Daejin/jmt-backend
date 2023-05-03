package com.gdsc.jmt.domain.restaurant.query.dto;

public record FindRestaurantResponse(
        Long id,
        String name,
        String placeUrl,
        String phone,

        String address,

        String roadAddress,

        String introduce,
        String category,

        String aggregateId
) { }
