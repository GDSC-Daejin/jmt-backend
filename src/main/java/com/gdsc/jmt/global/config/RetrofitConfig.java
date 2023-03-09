package com.gdsc.jmt.global.config;

import com.gdsc.jmt.global.http.AppleRestServerAPI;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitConfig {
    private static final String APPLE_URL = "https://appleid.apple.com/";

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    @Bean(name = "appleRetrofit")
    public Retrofit retrofit(OkHttpClient client) {
        return new Retrofit.Builder().baseUrl(APPLE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Bean
    public AppleRestServerAPI appleRestServerAPI(@Qualifier("appleRetrofit") Retrofit retrofit) {
        return retrofit.create(AppleRestServerAPI.class);
    }
}
