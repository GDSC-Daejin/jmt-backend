package com.gdsc.jmt.domain.restaurant.command.event;

import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequest;
import com.gdsc.jmt.global.event.BaseEvent;
import lombok.Getter;

@Getter
public class CreateRestaurantPhotoEvent extends BaseEvent<String> {

    private final String imageUrl;
    private final Long imageSize;

    public CreateRestaurantPhotoEvent(String id, String imageUrl, Long imageSize) {
        super(id);
        this.imageUrl = imageUrl;
        this.imageSize = imageSize;
    }
}
