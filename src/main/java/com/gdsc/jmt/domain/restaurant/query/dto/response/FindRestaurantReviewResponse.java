package com.gdsc.jmt.domain.restaurant.query.dto.response;

import com.gdsc.jmt.global.dto.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class FindRestaurantReviewResponse {
    private List<FindRestaurantReview> reviewList;
    private PageResponse page;
}
