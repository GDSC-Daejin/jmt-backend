package com.gdsc.jmt.domain.restaurant.command;

import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequest;
import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class CreateRestaurantPhotoCommand extends BaseCommand<String> {
    private final String imageUrl;
    private final Long imageSize;

    public CreateRestaurantPhotoCommand(String id, String imageUrl, Long imageSize) {
        super(id);
        this.imageUrl = imageUrl;
        this.imageSize = imageSize;
    }
}
