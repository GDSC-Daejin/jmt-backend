package com.gdsc.jmt.domain.restaurant.util;

import com.gdsc.jmt.domain.restaurant.query.dto.FindRestaurantLocationListRequest;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.http.KakaoRestServerAPI;
import com.gdsc.jmt.global.messege.DefaultMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestaurantAPIUtil {
    private final KakaoRestServerAPI kakaoRestServerAPI;
    @Value("${kakao.rest.api.key}")
    private String kakaoRestAPIKey;

    public KakaoSearchResponse findRestaurantLocation(final FindRestaurantLocationListRequest request) {
        try {
            Call<KakaoSearchResponse> call = kakaoRestServerAPI.sendSearchAPI(
                    "KakaoAK " + kakaoRestAPIKey,
                    request.query(),
                    "FD6",
                    request.page() != null ? request.page() : 1,
                    10,
                    request.x() != null ? request.x() : "",
                    request.y() != null ? request.y() : ""
            );

            Response<KakaoSearchResponse> response = call.execute();
            return response.body();
        } catch (IOException ex) {
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
