package com.gdsc.jmt.domain.restaurant.query.service;

import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.repository.RestaurantRepository;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchResponse;
import com.gdsc.jmt.domain.restaurant.util.RestaurantAPIUtil;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantQueryService {

    private final RestaurantAPIUtil restaurantAPIUtil;

    private final RestaurantRepository restaurantRepository;

    public List<KakaoSearchDocument> findRestaurantLocationList(final String query, Integer page) {
        if(page == null) {
            page = 1;
        }
        KakaoSearchResponse response = restaurantAPIUtil.findRestaurantLocation(query, page);
        return response.getDocuments();
    }

    public void checkRestaurantExisting(final String kakaoSubId) {
        Optional<RestaurantEntity> isExisting = restaurantRepository.findByKakaoSubId(kakaoSubId);
        if(isExisting.isPresent()) {
            throw new ApiException(RestaurantMessage.RESTAURANT_LOCATION_CONFLICT);
        }
    }
}
