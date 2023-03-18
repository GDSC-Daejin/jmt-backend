package com.gdsc.jmt.domain.restaurant.query.entity;

import com.gdsc.jmt.domain.restaurant.query.entity.embedded.Coordinate;
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
    private String name;

    @Column(nullable = false)
    private String address;

//    @Embedded
//    private Coordinate coordinate;
    private Point location;

    @Column(nullable = false, unique = true)
    private String aggregateId;

    @Builder
    public RestaurantEntity(String name, String address, Point location, String aggregateId) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.aggregateId = aggregateId;
    }
}
