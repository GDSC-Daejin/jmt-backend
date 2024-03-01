package com.gdsc.jmt.domain.restaurant.query.repository;

import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReviewEntity, Long> {

    Page<RestaurantReviewEntity> findByRecommendRestaurantId(Long recommendRestaurantId, Pageable pageable);
}
