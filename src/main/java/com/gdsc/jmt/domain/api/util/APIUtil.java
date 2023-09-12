package com.gdsc.jmt.domain.api.util;

import com.gdsc.jmt.domain.api.query.dto.request.NaverLocationConvertRequest;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.http.NaverRestServerAPI;
import com.gdsc.jmt.global.messege.DefaultMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class APIUtil {

    private final NaverRestServerAPI naverRestServerAPI;
    @Value("${naver.web.client.id}")
    private String naverWebClientId;

    @Value("${naver.web.client.secret}")
    private String naverWebClientSecret;

    public Object naverLocationConvert(final NaverLocationConvertRequest request) {
        try {
            Call<Object> call = naverRestServerAPI.sendLocationConvertAPI(
                    naverWebClientId, naverWebClientSecret,
                    request.coords(),
                    "legalcode,addr,admcode,roadaddr",
                    "json"
            );

            Response<Object> response = call.execute();
            return response.body();
        } catch (IOException ex) {
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
