package com.gdsc.jmt.domain.restaurant.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
public class RecommendRestaurantRequest {
        private final String introduce;
        private final Long categoryId;
//        private final List<MultipartFile> pictures;

        private final Boolean canDrinkLiquor;

        private final String goWellWithLiquor;

        private final String recommendMenu;

        @Builder
        RecommendRestaurantRequest(String introduce,
                                   Long categoryId,
                                   List<MultipartFile> pictures,
                                   Boolean canDrinkLiquor,
                                   String goWellWithLiquor,
                                   String recommendMenu) {
            this.introduce = introduce;
            this.categoryId = categoryId;
//            this.pictures = pictures;
            this.canDrinkLiquor = canDrinkLiquor;
            this.goWellWithLiquor = goWellWithLiquor;
            this.recommendMenu = recommendMenu;
        }
}
