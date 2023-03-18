package com.gdsc.jmt.domain.restaurant.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// TODO : 네이버 API 연동 전 로직
@Getter
@Setter
public class CreateRestaurantRequest {
        // 기본 식당 정보
        @Schema(description = "임시 식당 이름", example = "마라탕")
        private String name;

        // 사용자가 입력한 맛집 정보
        @Schema(description = "임시 식당 이름", example = "마라탕")
        private String introduce;
        @Schema(description = "임시 식당 이름", example = "1")
        private Long categoryId;
        @Schema(description = "사진 데이터")
        private List<MultipartFile> pictures;

        @Schema(description = "주류 유무", example = "true")
        private Boolean canDrinkLiquor;

        @Schema(description = "어울리는 술", example = "위스키")
        private String goWellWithLiquor;

        @Schema(description = "추천 메뉴", example = "#마라탕#양꼬치")
        private String recommendMenu;

}
