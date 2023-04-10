package com.gdsc.jmt.global.http;

import com.gdsc.jmt.domain.restaurant.util.KakaoSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface KakaoRestServerAPI {
    @GET("local/search/keyword.json")
    Call<KakaoSearchResponse> sendSearchAPI(
            @Header("Authorization") String restAPIKey,
            @Query("query") String query,
            @Query("category_group_code") String category,
            @Query("page") Integer page,
            @Query("size") Integer size,
            @Query("x") String x,
            @Query("y") String y);
}
