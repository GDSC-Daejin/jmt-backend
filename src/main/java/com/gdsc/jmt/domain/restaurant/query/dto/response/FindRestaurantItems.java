package com.gdsc.jmt.domain.restaurant.query.dto.response;

public record FindRestaurantItems(
        Long id,
        String name,
        String placeUrl,
        String phone,

        String address,

        String roadAddress,
        Double x,
        Double y,
        String introduce,
        String category,
        String userNickName,
        String userProfileImageUrl
) { }
