package com.gdsc.jmt.domain.restaurant.query.entity.calculate;

import com.gdsc.jmt.domain.restaurant.query.dto.response.FindRestaurantItems;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;

public class RecommendRestaurantWithDistanceDTO {
    private final RecommendRestaurantEntity recommendRestaurant;
    private final String differenceInDistance;

    public RecommendRestaurantWithDistanceDTO(RecommendRestaurantEntity recommendRestaurant, Object differenceInDistance) {
        this.recommendRestaurant = recommendRestaurant;
        if(differenceInDistance instanceof Double) {
            String distanceParse = String.valueOf(differenceInDistance);
            this.differenceInDistance = distanceParse.split("\\.")[0];
        }
        else {
            this.differenceInDistance = "";
        }
    }

    public FindRestaurantItems convertToFindItems() {
        return FindRestaurantItems.createWithDistance(recommendRestaurant.convertToFindItems(), differenceInDistance);
    }
}
