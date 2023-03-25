package com.gdsc.jmt.domain.restaurant.util;

import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.http.KakaoRestServerAPI;
import com.gdsc.jmt.global.messege.DefaultMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestaurantAPIUtil {
//    private static NaverRestServerAPI naverRestServerAPI;
    private final KakaoRestServerAPI kakaoRestServerAPI;

//    @Value("naver.client.id")
//    private static String naverClientId;
//
//    @Value("naver.client.secret")
//    private static String naverClientSecret;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestAPIKey;

    public KakaoSearchResponse findRestaurantLocation(final String query, final Integer page) {
        try {
            Call<KakaoSearchResponse> call = kakaoRestServerAPI.sendSearchAPI(
                    "KakaoAK " + kakaoRestAPIKey,
                    query,
                    "FD6",
                    page,
                    10
            );

            Response<KakaoSearchResponse> response = call.execute();
            return response.body();
        } catch (IOException ex) {
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
