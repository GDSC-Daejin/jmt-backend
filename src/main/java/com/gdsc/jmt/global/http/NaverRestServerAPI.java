package com.gdsc.jmt.global.http;

import com.gdsc.jmt.domain.restaurant.naver.NaverLocationResponse;
import com.gdsc.jmt.domain.user.apple.Keys;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NaverRestServerAPI {
    @GET("search/local.json")
    Call<NaverLocationResponse> sendLocationAPI(
                       @Header("X-Naver-Client-Id") String clientId,
                       @Header("X-Naver-Client-Secret") String clientSecret,
                       @Query("query") String query,
                       @Query("display") Integer display);
}
