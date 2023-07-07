package com.gdsc.jmt.domain.restaurant.query.dto.response;

public record FindDetailRestaurantItem(
        //식당 정보
        String name,
        String placeUrl,
        String category,
        String phone,
        String address,
        String roadAddress,
        double x,
        double y,
//        String image,

        // 식당 추천 정보
        Boolean canDrinkLiquor,
        String goWellWithLiquor,
        String recommendMenu

) { }