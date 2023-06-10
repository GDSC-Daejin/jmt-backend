package com.gdsc.jmt.domain.restaurant.query.dto.response;

import com.gdsc.jmt.global.dto.PageResponse;

import java.util.List;

public record FindRestaurantResponse(
        List<FindRestaurantItems> restaurants,
        PageResponse page
) { }
