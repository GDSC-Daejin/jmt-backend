package com.gdsc.jmt.domain.restaurant.query.entity;

import com.gdsc.jmt.domain.category.query.entity.CategoryEntity;
import com.gdsc.jmt.domain.restaurant.command.dto.request.UpdateRecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.query.dto.response.FindDetailRestaurantItem;
import com.gdsc.jmt.domain.restaurant.query.dto.response.FindRestaurantItems;

import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Formula;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

@NoArgsConstructor
@Getter
@Entity @Table(name = "tb_recommend_restaurant")
public class RecommendRestaurantEntity extends BaseTimeEntity {
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

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "recommend_restaurant_id")
    private List<RestaurantPhotoEntity> pictures = new ArrayList<>();

    @Column(nullable = false)
    private Boolean canDrinkLiquor;

    private String goWellWithLiquor;

    private String recommendMenu;

    @Formula("select ST_DISTANCE_SPHERE(reRestaurant.restaurant.location, ST_GeomFromText(reRestaurant.userLocation))"
            + " from RecommendRestaurantEntity reRestaurant")
    private String distance;

    @Transient
    private Point userLocation;

    public void setUserLocation(Double x, Double y) throws ParseException {
        this.userLocation = (Point)  new WKTReader().read(String.format("POINT(%s %s)", x, y));
    }

    @Builder
    RecommendRestaurantEntity(String introduce,
                              CategoryEntity category,
                              UserEntity user,
                              RestaurantEntity restaurant,
                              List<RestaurantPhotoEntity> pictures,
                              Boolean canDrinkLiquor,
                              String goWellWithLiquor,
                              String recommendMenu) {
        this.introduce = introduce;
        this.category = category;
        this.user = user;
        this.restaurant = restaurant;
        this.pictures = pictures;
        this.canDrinkLiquor = canDrinkLiquor;
        this.goWellWithLiquor = goWellWithLiquor;
        this.recommendMenu = recommendMenu;
    }

    public void initPictures(List<RestaurantPhotoEntity> pictures) {
        this.pictures = pictures;
    }

    public FindDetailRestaurantItem toResponse() {
        return new FindDetailRestaurantItem(
                this.restaurant.getName(),
                this.restaurant.getPlaceUrl(),
                this.category.getName(),
                this.restaurant.getPhone(),
                this.restaurant.getAddress(),
                this.restaurant.getRoadAddress(),
                this.restaurant.getLocation().getX(),
                this.restaurant.getLocation().getY(),
                this.introduce,
                this.canDrinkLiquor,
                this.goWellWithLiquor,
                this.recommendMenu,
                this.pictures.stream().map(RestaurantPhotoEntity::getImageUrl).toList(),
                this.user.getId(),
                this.user.getNickname(),
                this.user.getProfileImageUrl()
        );
    }

    public FindRestaurantItems convertToFindItems() {
        String restaurantImageUrl = null;
        if(!this.pictures.isEmpty()) {
            restaurantImageUrl = this.pictures.get(0).getImageUrl();
        }
        return FindRestaurantItems.createDefault(
                this.id,
                this.restaurant.getName(),
                this.restaurant.getPlaceUrl(),
                this.restaurant.getPhone(),
                this.restaurant.getAddress(),
                this.restaurant.getRoadAddress(),
                this.restaurant.getLocation().getX(),
                this.restaurant.getLocation().getY(),
                restaurantImageUrl,
                this.introduce,
                this.category.getName(),
                this.user.getNickname(),
                this.user.getProfileImageUrl(),
                this.canDrinkLiquor
        );
    }

    public void update(UpdateRecommendRestaurantRequest updateRequest, CategoryEntity category) {
        this.introduce = updateRequest.getIntroduce();
        this.category = category;
        this.canDrinkLiquor = updateRequest.getCanDrinkLiquor();
        this.goWellWithLiquor = updateRequest.getGoWellWithLiquor();
        this.recommendMenu = updateRequest.getRecommendMenu();
    }
}
