package com.gdsc.jmt.domain.user.util;

import com.gdsc.jmt.domain.user.query.dto.FindLocationListRequest;
import com.gdsc.jmt.domain.user.query.dto.KakaoSearchLocationResponse;
import com.gdsc.jmt.domain.user.query.dto.KakaoUserLocationResponse;
import com.gdsc.jmt.domain.user.query.dto.UserLocationRequest;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.http.KakaoRestServerAPI;
import com.gdsc.jmt.global.messege.DefaultMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

@Component
@RequiredArgsConstructor
public class LocationAPIUtil {
    private final KakaoRestServerAPI kakaoRestServerAPI;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestAPIKey;

    public KakaoSearchLocationResponse findLocation(final FindLocationListRequest request) {
        try {
            Call<KakaoSearchLocationResponse> call = kakaoRestServerAPI.sendSearchAPI(
                    "KakaoAK " + kakaoRestAPIKey,
                    request.query(),
                    request.page() != null ? request.page() : 1,
                    15
            );

            Response<KakaoSearchLocationResponse> response = call.execute();
            return response.body();
        }
        catch (Exception ex) {
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public KakaoUserLocationResponse getCurrentLocation(final UserLocationRequest request) {
        try {
            Call<KakaoUserLocationResponse> call = kakaoRestServerAPI.sendCurrentLocationAPI(
                    "KakaoAK " + kakaoRestAPIKey,
                    request.x() != null ? request.x() : "",
                    request.y() != null ? request.y() : ""
            );

            Response<KakaoUserLocationResponse> response = call.execute();
            return response.body();
        }
        catch (Exception ex) {
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
