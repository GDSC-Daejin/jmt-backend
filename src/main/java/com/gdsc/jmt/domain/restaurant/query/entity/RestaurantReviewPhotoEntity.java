package com.gdsc.jmt.domain.restaurant.query.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_restaurant_review_photo")
public class RestaurantReviewPhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;  // 파일 저장 경로

    @Column(name = "restaurant_review_id")
    private Long restaurantReviewId;
}
