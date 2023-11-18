package com.gdsc.jmt.domain.restaurant.query.entity;

import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.global.entity.BaseTimeEntity;
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
@Table(name = "tb_report")
public class ReportEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="report_restaurant_id", nullable = false)
    private RecommendRestaurantEntity reportRestaurant;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="report_user_id", nullable = false)
    private UserEntity reportUser;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="reporter_user_id", nullable = false)
    private UserEntity reporterUser;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="report_reason_id", nullable = false)
    private ReportReasonEntity reportReason;

    private String reportReasonText;
}
