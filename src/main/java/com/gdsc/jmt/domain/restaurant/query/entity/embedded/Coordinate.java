package com.gdsc.jmt.domain.restaurant.query.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Coordinate {

    @Column(nullable = false)
    private Long latitude;   // 위도

    @Column(nullable = false)
    private Long longitude;  // 경도
}
