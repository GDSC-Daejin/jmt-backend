package com.gdsc.jmt.domain.restaurant.query.repository;

import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.calculate.RecommendRestaurantWithDistanceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecommendRestaurantRepository extends JpaRepository<RecommendRestaurantEntity, Long> {
    Optional<RecommendRestaurantEntity> findByRestaurant(RestaurantEntity restaurant);

    @Query("select reRestaurant from RecommendRestaurantEntity reRestaurant " +
            "where reRestaurant.restaurant.name LIKE %:keyword%")
//            "or reRestaurant.introduce LIKE %:keyword% " +
//            "or reRestaurant.recommendMenu LIKE %:keyword% ")
    Page<RecommendRestaurantEntity> findSearch(String keyword, Pageable pageable);

    @Query("select NEW com.gdsc.jmt.domain.restaurant.query.entity.calculate.RecommendRestaurantWithDistanceDTO(reRestaurant, ST_DISTANCE_SPHERE(reRestaurant.restaurant.location, ST_GeomFromText(:userLocation))) " +
            "from RecommendRestaurantEntity reRestaurant " +
            "where reRestaurant.user.id = :userId")
    Page<RecommendRestaurantWithDistanceDTO> findByUserId(Long userId, String userLocation, Pageable pageable);

//    @EntityGraph(attributePaths = {"category", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
//    Page<RecommendRestaurantEntity> findAllWithPage(Pageable pageable);
}
