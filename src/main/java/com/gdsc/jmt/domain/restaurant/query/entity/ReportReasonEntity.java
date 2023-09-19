package com.gdsc.jmt.domain.restaurant.query.entity;

import com.gdsc.jmt.domain.restaurant.query.dto.response.FindReportReasonResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_report_reason")
public class ReportReasonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    private String reportReason;

    public FindReportReasonResponse convertResponse() {
        return new FindReportReasonResponse(id, reportReason);
    }
}
