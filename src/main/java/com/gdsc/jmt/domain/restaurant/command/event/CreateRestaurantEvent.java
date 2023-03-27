package com.gdsc.jmt.domain.restaurant.command.event;

import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.global.event.BaseEvent;
import lombok.Getter;

@Getter
public class CreateRestaurantEvent extends BaseEvent<String> {
    private final KakaoSearchDocument kakaoSearchDocument;

    public CreateRestaurantEvent(String id, KakaoSearchDocument kakaoSearchDocument) {
        super(id);
        this.kakaoSearchDocument = kakaoSearchDocument;
    }
}
