package com.gdsc.jmt.domain.user.util.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoadAddress {
    String address_name;
    String region_1depth_name;
    String region_2depth_name;
    String region_3depth_name;
    String road_name;
    String underground_yn;
    String main_building_no;
    String sub_building_no;
    String building_name;
    String zone_no;
}
