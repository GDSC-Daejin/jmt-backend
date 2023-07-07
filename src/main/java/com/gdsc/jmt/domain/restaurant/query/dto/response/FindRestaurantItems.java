package com.gdsc.jmt.domain.restaurant.query.dto.response;

public record FindRestaurantItems(
        Long id,
        String name,
        String placeUrl,
        String phone,

        String address,

        String roadAddress,

        String introduce,
        String category
) { }
