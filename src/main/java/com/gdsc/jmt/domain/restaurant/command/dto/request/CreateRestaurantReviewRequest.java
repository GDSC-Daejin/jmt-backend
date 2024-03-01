package com.gdsc.jmt.domain.restaurant.command.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateRestaurantReviewRequest {
    private String reviewContent;

    private List<MultipartFile> reviewImages;
}
