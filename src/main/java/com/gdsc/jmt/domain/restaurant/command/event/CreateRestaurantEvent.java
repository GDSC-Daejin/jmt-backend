package com.gdsc.jmt.domain.restaurant.command.event;

import com.gdsc.jmt.global.event.BaseEvent;
import lombok.Getter;

@Getter
public class CreateRestaurantEvent extends BaseEvent<String> {
    // TODO : 해당 필드들은 네이버 API 연동 이전 데이터들입니다.
    // TODO : 따라서 네이버 API 연동 단계에서 Response 확인하고 나중에 DTO로 변경하기
    private final String name;
    private final String address;

    private final Double latitude;   // 위도
    private final Double longitude;  // 경도

    public CreateRestaurantEvent(String id, String name, String address, Double latitude, Double longitude) {
        super(id);
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
