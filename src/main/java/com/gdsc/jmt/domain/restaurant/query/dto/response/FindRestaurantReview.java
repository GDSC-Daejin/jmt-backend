package com.gdsc.jmt.domain.restaurant.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindRestaurantReview {

    private Long reviewId;

    private Long recommendRestaurantId;

    private String userName;

    private String reviewContent;

    private List<String> reviewImages;

    private String reviewerImageUrl;
}
