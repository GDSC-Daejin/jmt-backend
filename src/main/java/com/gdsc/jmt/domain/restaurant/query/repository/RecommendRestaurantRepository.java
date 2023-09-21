package com.gdsc.jmt.domain.restaurant.query.repository;

import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.calculate.RecommendRestaurantWithDistanceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecommendRestaurantRepository extends JpaRepository<RecommendRestaurantEntity, Long> {
    Optional<RecommendRestaurantEntity> findByRestaurant(RestaurantEntity restaurant);

    @Query("select reRestaurant from RecommendRestaurantEntity reRestaurant " +
            "where reRestaurant.restaurant.name LIKE %:keyword%")
    Page<RecommendRestaurantEntity> findSearch(String keyword, Pageable pageable);

    @Query("select reRestaurant from RecommendRestaurantEntity reRestaurant " +
            "WHERE ST_Within(reRestaurant.restaurant.location, ST_GeomFromText(:locationRange))" +
            "AND reRestaurant.restaurant.name LIKE %:keyword%")
    Page<RecommendRestaurantEntity> findSearchWithinDistance(String keyword, String locationRange, Pageable pageable);

    @Query("select NEW com.gdsc.jmt.domain.restaurant.query.entity.calculate.RecommendRestaurantWithDistanceDTO(reRestaurant, ST_DISTANCE_SPHERE(reRestaurant.restaurant.location, ST_GeomFromText(:userLocation))) " +
            "from RecommendRestaurantEntity reRestaurant " +
            "where reRestaurant.user.id = :userId")
    Page<RecommendRestaurantWithDistanceDTO> findByUserId(Long userId, String userLocation, Pageable pageable);

    @EntityGraph(attributePaths = {"restaurant", "category"})
    @Query("SELECT reRestaurant FROM RecommendRestaurantEntity reRestaurant " +
            "WHERE ST_Within(reRestaurant.restaurant.location, ST_GeomFromText(:locationRange))" +
            "AND reRestaurant.category.id IN (:categoryIds)" +
            "AND reRestaurant.canDrinkLiquor = :isCanDrinkLiquor")
    Page<RecommendRestaurantEntity> findByLocationWithinDistance(String locationRange, List<Long> categoryIds, boolean isCanDrinkLiquor , Pageable pageable);

    @EntityGraph(attributePaths = {"restaurant", "category"})
    @Query("SELECT reRestaurant FROM RecommendRestaurantEntity reRestaurant " +
            "WHERE ST_Within(reRestaurant.restaurant.location, ST_GeomFromText(:locationRange))" +
            "AND reRestaurant.category.id IN (:categoryIds)")
    Page<RecommendRestaurantEntity> findByLocationWithinDistance(String locationRange, List<Long> categoryIds, Pageable pageable);
}
