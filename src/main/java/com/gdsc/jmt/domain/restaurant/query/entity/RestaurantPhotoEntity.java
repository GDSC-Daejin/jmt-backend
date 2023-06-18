package com.gdsc.jmt.domain.restaurant.query.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity @Table(name = "tb_restaurant_photo")
public class RestaurantPhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String imageUrl;  // 파일 저장 경로

    @Column(nullable = false)
    private Long imageSize;

//    @ManyToOne
//    @JoinColumn(name = "recommend_restaurnat_id")
//    private RecommendRestaurantEntity recommendRestaurant;

    @Builder
    public RestaurantPhotoEntity(String aggregateId, String imageUrl, Long imageSize){
        this.aggregateId = aggregateId;
        this.imageUrl = imageUrl;
        this.imageSize = imageSize;
    }

//    public void initRecommendRestaurant(RecommendRestaurantEntity recommendRestaurant){
//        this.recommendRestaurant = recommendRestaurant;
//    }
}
