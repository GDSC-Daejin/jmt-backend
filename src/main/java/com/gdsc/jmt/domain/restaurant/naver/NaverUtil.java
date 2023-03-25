package com.gdsc.jmt.domain.restaurant.naver;

import com.gdsc.jmt.global.http.NaverRestServerAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

@Component
public class NaverUtil {
    private static NaverRestServerAPI naverRestServerAPI;

    @Value("naver.client.id")
    private static String naverClientId;

    @Value("naver.client.secret")
    private static String naverClientSecret;

    @Autowired
    public NaverUtil(NaverRestServerAPI naverRestServerAPI) {
        NaverUtil.naverRestServerAPI = naverRestServerAPI;
    }

    public static List<NaverLocationItem> findRestaurantLocation(final String query) {
        try {
            Call<NaverLocationResponse> call = naverRestServerAPI.sendLocationAPI(
                    naverClientId,
                    naverClientSecret,
                    query,
                    5
            );

            Response<NaverLocationResponse> response = call.execute();
        } catch (IOException ex) {
        }
    }
}
