package com.gdsc.jmt.domain.user.util.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Address {
    String address_name;
    String region_1depth_name;
    String region_2depth_name;
    String region_3depth_name;
    String mountain_yn;
    String main_address_no;
    String sub_address_no;
}
