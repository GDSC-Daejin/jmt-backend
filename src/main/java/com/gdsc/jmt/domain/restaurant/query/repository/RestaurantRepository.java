package com.gdsc.jmt.domain.restaurant.query.repository;

import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findByName(String name);

    Optional<RestaurantEntity> findByKakaoSubId(String kakaoSubId);

    @Query("SELECT restaurant FROM RestaurantEntity restaurant " +
            "WHERE ST_Distance(ST_Point(:userLongitude, :user_latitude), restaurant.location) <= :distance")
    List<RestaurantEntity> findByLocationWithinDistance(double userLongitude, double user_latitude, Integer distance);
}
