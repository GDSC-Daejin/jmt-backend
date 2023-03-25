package com.gdsc.jmt.domain.restaurant.util;

import lombok.Getter;

@Getter
public class KaKaoSearchDocument {

    private String place_name;
    private String distance;

    private String place_url;

    private String category_name;

    private String address_name;

    private String road_address_name;

    private String id;

    private String phone;

    private String category_group_code;

    private String category_group_name;

    private String x;
    private String y;
}
