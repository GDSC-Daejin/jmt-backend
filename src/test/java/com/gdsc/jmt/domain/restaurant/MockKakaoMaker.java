package com.gdsc.jmt.domain.restaurant;

import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchResponse;

import java.util.ArrayList;
import java.util.List;

public class MockKakaoMaker {

    public static KakaoSearchResponse makeMockKaKaoApiResponse() {
        List<KakaoSearchDocument> kakaoSearchDocuments = new ArrayList<>();
        KakaoSearchDocument document = makeMockKakaoSearchDocument();
        kakaoSearchDocuments.add(document);
        return new KakaoSearchResponse(null, kakaoSearchDocuments);
    }

    public static KakaoSearchDocument makeMockKakaoSearchDocument() {
        return new KakaoSearchDocument(
                "마제소바",
                "0",
                "http://place.map.kakao.com/1574464357",
                "음식점 > 일식",
                "대구 동구 신천동 384-12",
                "대구 동구 동부로 158-7",
                "1574464357",
                "",
                "FD6",
                "음식점",
                "128.629555401333",
                "35.8768967936303");
    }
}
