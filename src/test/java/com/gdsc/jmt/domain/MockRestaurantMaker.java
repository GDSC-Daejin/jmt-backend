package com.gdsc.jmt.domain;

import com.gdsc.jmt.domain.category.query.entity.CategoryEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class MockRestaurantMaker {

    public static RestaurantEntity makeMockRestaurantEntity(String kakaoSubId) {
        try {
            String pointWKT = String.format("POINT(%s %s)", 1.2, 3.4);
            Point testLocation = (Point) new WKTReader().read(pointWKT);

            return RestaurantEntity.builder()
                    .kakaoSubId(kakaoSubId)
                    .name("마제소바 맛집")
                    .placeUrl("https://test맛집.com")
                    .category("양식")
                    .phone("010-1234-5678")
                    .address("서울 노원구")
                    .roadAddress("도로명 주소")
                    .location(testLocation)
                    .aggregateId("123456789")
                    .build();
        }
        catch (ParseException ex) {
            return null;
        }
    }

    public static RecommendRestaurantEntity makcMockRecommendRestaurantEntity(RestaurantEntity restaurant) {
        CategoryEntity category = new CategoryEntity();
        category.initForTest(1L, "중식");
        return RecommendRestaurantEntity.builder()
                .introduce("마라탕")
                .category(category)
                .restaurant(restaurant)
                .canDrinkLiquor(true)
                .goWellWithLiquor("위스키")
                .recommendMenu("#마제소바#라멘")
                .aggregateId("1234-5678-87")
                .build();
    }
}
