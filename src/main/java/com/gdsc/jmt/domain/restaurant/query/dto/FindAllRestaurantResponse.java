package com.gdsc.jmt.domain.restaurant.query.dto;

import com.gdsc.jmt.domain.restaurant.query.dto.response.FindRestaurantItems;

import java.util.List;

public record FindAllRestaurantResponse(
        List<FindRestaurantItems> restaurants,
        PageMeta page
) { }
