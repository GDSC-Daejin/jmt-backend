package com.gdsc.jmt.domain.restaurant.query.entity;

import com.gdsc.jmt.domain.category.query.entity.CategoryEntity;
import com.gdsc.jmt.domain.restaurant.query.dto.response.FindDetailRestaurantItem;
import com.gdsc.jmt.domain.restaurant.query.dto.response.FindRestaurantItems;

import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity @Table(name = "tb_recommend_restaurant")
public class RecommendRestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String introduce;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="category_id", nullable = false)
    private CategoryEntity category;

    @OneToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

    @OneToOne
    @JoinColumn(name="restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommend_restaurant_id")
    private List<RestaurantPhotoEntity> pictures = new ArrayList<>();

    @Column(nullable = false)
    private Boolean canDrinkLiquor;

    private String goWellWithLiquor;

    private String recommendMenu;

    @Column(unique = true, nullable = false)
    private String aggregateId;

    @Builder
    RecommendRestaurantEntity(String introduce,
                              CategoryEntity category,
                              UserEntity user,
                              RestaurantEntity restaurant,
                              List<RestaurantPhotoEntity> pictures,
                              Boolean canDrinkLiquor,
                              String goWellWithLiquor,
                              String recommendMenu,
                              String aggregateId) {
        this.introduce = introduce;
        this.category = category;
        this.user = user;
        this.restaurant = restaurant;
        if(pictures != null)
            this.pictures = pictures;
        this.pictures = pictures;
        this.canDrinkLiquor = canDrinkLiquor;
        this.goWellWithLiquor = goWellWithLiquor;
        this.recommendMenu = recommendMenu;
        this.aggregateId = aggregateId;
    }

//    private void initPictures(List<RestaurantPhotoEntity> pictures) {
//        for(RestaurantPhotoEntity picture : pictures) {
//            picture.initRecommendRestaurant(this);
//        }
//    }

    public FindDetailRestaurantItem toResponse() {
        return new FindDetailRestaurantItem(
                restaurant.getName(),
                restaurant.getPlaceUrl(),
                category.getName(),
                restaurant.getPhone(),
                restaurant.getAddress(),
                restaurant.getRoadAddress(),
                restaurant.getLocation().getX(),
                restaurant.getLocation().getY(),
//                restaurant.getImage(),
                canDrinkLiquor,
                goWellWithLiquor,
                recommendMenu,
                aggregateId
        );
    }

    public FindRestaurantItems convertToFindItems() {
        return new FindRestaurantItems(
                this.id,
                this.restaurant.getName(),
                this.restaurant.getPlaceUrl(),
                this.restaurant.getPhone(),
                this.restaurant.getAddress(),
                this.restaurant.getRoadAddress(),
                this.introduce,
                this.category.getName(),
                this.aggregateId
        );
    }
}
