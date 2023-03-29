package com.gdsc.jmt.domain.restaurant.command;

import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.global.command.BaseCommand;
import lombok.Getter;

@Getter
public class CreateRestaurantCommand extends BaseCommand<String> {
    private final KakaoSearchDocument kakaoSearchDocument;

    public CreateRestaurantCommand(String id, KakaoSearchDocument kakaoSearchDocument) {
        super(id);
        this.kakaoSearchDocument = kakaoSearchDocument;
    }
}
