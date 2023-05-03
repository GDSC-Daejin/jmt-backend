package com.gdsc.jmt.domain.restaurant.query.repository;

import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRestaurantRepository extends JpaRepository<RecommendRestaurantEntity, Long> {
    Optional<RecommendRestaurantEntity> findByRestaurant(RestaurantEntity restaurant);

//    @EntityGraph(attributePaths = {"category", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
//    Page<RecommendRestaurantEntity> findAllWithPage(Pageable pageable);
}
