package com.gdsc.jmt.domain.restaurant.command.dto;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public record RecommendRestaurantRequest(
         String introduce,
         Long categoryId,
         List<MultipartFile>pictures,
         Boolean canDrinkLiquor,
         String goWellWithLiquor,
         String recommendMenu
) { }
