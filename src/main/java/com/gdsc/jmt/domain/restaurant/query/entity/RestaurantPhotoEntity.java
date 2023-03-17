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

    @Column(nullable = false)
    private String origFileName;  // 파일 원본명

    @Column(nullable = false)
    private String fileUrl;  // 파일 저장 경로

    private Long fileSize;

    @ManyToOne
    @JoinColumn(name = "recommend_restaurnat_id")
    private RecommendRestaurantEntity recommendRestaurant;

    @Builder
    public RestaurantPhotoEntity(String origFileName, String fileUrl, Long fileSize){
        this.origFileName = origFileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
    }

    public void initRecommendRestaurant(RecommendRestaurantEntity recommendRestaurant){
        this.recommendRestaurant = recommendRestaurant;
    }
}
