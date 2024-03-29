package com.gdsc.jmt.domain.restaurant.query.entity;


import com.gdsc.jmt.domain.restaurant.query.dto.response.FindRestaurantReview;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_restaurant_review")
public class RestaurantReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "restaurant_id")
    private Long recommendRestaurantId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "review_content")
    private String reviewContent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_review_id")
    private List<RestaurantReviewPhotoEntity> pictures;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    private RecommendRestaurantEntity recommendRestaurant;

    public FindRestaurantReview toResponse() {
        return FindRestaurantReview.builder()
                .reviewId(this.id)
                .recommendRestaurantId(this.recommendRestaurantId)
                .userName(user.getNickname())
                .reviewContent(this.reviewContent)
                .reviewImages(pictures.stream().map(RestaurantReviewPhotoEntity::getImageUrl).toList())
                .reviewerImageUrl(user.getProfileImageUrl())
                .groupId(recommendRestaurant.getGroup().getGid())
                .groupName(recommendRestaurant.getGroup().getGroupName())
                .build();
    }
}
