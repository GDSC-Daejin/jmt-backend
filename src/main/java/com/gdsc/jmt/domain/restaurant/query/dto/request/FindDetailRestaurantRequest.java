package com.gdsc.jmt.domain.restaurant.query.dto.request;

import lombok.Data;

@Data
public class FindDetailRestaurantRequest {
    private MapLocation userLocation;
}
