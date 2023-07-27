package com.gdsc.jmt.domain.restaurant.query.repository;

import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findByName(String name);

    Optional<RestaurantEntity> findByKakaoSubId(String kakaoSubId);

    @Query("SELECT restaurant FROM RestaurantEntity restaurant " +
            "WHERE ST_DISTANCE_SPHERE(restaurant.location, ST_GeomFromText(:userLocation)) <= :distance")
    List<RestaurantEntity> findByLocationWithinDistance(String userLocation, Integer distance);
}
