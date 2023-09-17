package com.gdsc.jmt.domain.restaurant.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReportRecommendRestaurantRequest {
    @Schema(description = "신고 사유", example = "홍보성 게시물")
    private String reportReason;
}
