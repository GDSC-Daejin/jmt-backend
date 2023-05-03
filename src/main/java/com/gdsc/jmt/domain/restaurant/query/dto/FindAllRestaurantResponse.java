package com.gdsc.jmt.domain.restaurant.query.dto;

import java.util.List;

public record FindAllRestaurantResponse(
        List<FindRestaurantResponse> restaurants,
        PageMeta page
) { }
