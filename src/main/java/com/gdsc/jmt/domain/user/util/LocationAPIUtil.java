package com.gdsc.jmt.domain.user.util;

import com.gdsc.jmt.domain.user.query.dto.FindLocationListRequest;
import com.gdsc.jmt.domain.user.query.dto.KakaoLocationResponse;
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

    public KakaoLocationResponse findLocation(final FindLocationListRequest request) {
        try {
            Call<KakaoLocationResponse> call = kakaoRestServerAPI.sendSearchAPI(
                    "KakaoAK " + kakaoRestAPIKey,
                    request.query(),
                    request.page() != null ? request.page() : 1,
                    10
            );

            Response<KakaoLocationResponse> response = call.execute();
            return response.body();
        }
        catch (Exception ex) {
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        }
    }

}
