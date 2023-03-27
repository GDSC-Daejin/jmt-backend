package com.gdsc.jmt.domain.restaurant.query.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@NoArgsConstructor
@Entity @Table(name = "tb_restaurant")
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(nullable = false, unique = true)
    private String kakaoSubId;

    @Column(nullable = false, unique = true)
    private String name;

    private String placeUrl;

    private String category;

    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String roadAddress;

    @Column(nullable = false)
    private Point location;

    @Column(nullable = false, unique = true)
    private String aggregateId;

    @Builder
    public RestaurantEntity(String kakaoSubId,
                            String name,
                            String placeUrl,
                            String category,
                            String phone,
                            String address,
                            String roadAddress,
                            Point location,
                            String aggregateId) {
        this.kakaoSubId = kakaoSubId;
        this.name = name;
        this.placeUrl = placeUrl;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.roadAddress = roadAddress;
        this.location = location;
        this.aggregateId = aggregateId;
    }
}
