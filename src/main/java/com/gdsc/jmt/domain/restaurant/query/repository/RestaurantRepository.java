package com.gdsc.jmt.domain.restaurant.query.repository;

import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findByName(String name);

    Optional<RestaurantEntity> findByKakaoSubId(String kakaoSubId);
}
