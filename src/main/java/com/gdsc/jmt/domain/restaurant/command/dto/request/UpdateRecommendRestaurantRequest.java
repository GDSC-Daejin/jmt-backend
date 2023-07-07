package com.gdsc.jmt.domain.restaurant.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateRecommendRestaurantRequest {
    @Schema(description = "맛집 id", example = "1")
    private Long id;
    @Schema(description = "식당 소개글", example = "마제소바")
    private String introduce;
    @Schema(description = "식당 카테고리 ID", example = "1")
    private Long categoryId;
    @Schema(description = "주류 유무", example = "true")
    private Boolean canDrinkLiquor;

    @Schema(description = "어울리는 술", example = "위스키")
    private String goWellWithLiquor;

    @Schema(description = "추천 메뉴", example = "#마제소바#라멘")
    private String recommendMenu;
}
