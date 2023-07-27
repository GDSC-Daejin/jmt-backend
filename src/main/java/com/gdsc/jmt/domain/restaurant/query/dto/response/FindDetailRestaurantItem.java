package com.gdsc.jmt.domain.restaurant.query.dto.response;

import java.util.List;

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
        // 식당 추천 정보
        String introduce,
        Boolean canDrinkLiquor,
        String goWellWithLiquor,
        String recommendMenu,
        List<String> pictures,


        //식당 등록한 유저 정보
        Long userId,
        String userNickName,
        String userProfileImageUrl

) { }