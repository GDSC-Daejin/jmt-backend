package com.gdsc.jmt.domain.restaurant.query.service;

import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchResponse;
import com.gdsc.jmt.domain.restaurant.util.RestaurantAPIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantQueryService {

    private final RestaurantAPIUtil restaurantAPIUtil;

    public List<KakaoSearchDocument> findRestaurantLocationList(final String query, final Integer page) {
         KakaoSearchResponse response = restaurantAPIUtil.findRestaurantLocation(query, page);
         return response.getDocuments();
    }
}
