package com.gdsc.jmt.domain.restaurant.query.repository;

import com.gdsc.jmt.domain.restaurant.query.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
}
