package com.gdsc.jmt.domain.restaurant.query.service;

import com.gdsc.jmt.domain.restaurant.query.dto.FindRestaurantLocationListRequest;
import com.gdsc.jmt.domain.restaurant.query.dto.response.FindDetailRestaurantResponse;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.repository.RecommendRestaurantRepository;
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

    // 혹시 해당 조회 기능이 늘어나는 경우 서비스 분리 필요
    private final RecommendRestaurantRepository recommendRestaurantRepository;


    public List<KakaoSearchDocument> findRestaurantLocationList(final FindRestaurantLocationListRequest findRestaurantLocationListRequest) {
        KakaoSearchResponse response = restaurantAPIUtil.findRestaurantLocation(findRestaurantLocationListRequest);
        return response.getDocuments();
    }

    public void checkRecommendRestaurantExisting(final String kakaoSubId) {
        RestaurantEntity restaurant = findRestaurant(kakaoSubId);
        Optional<RecommendRestaurantEntity> isExisting = recommendRestaurantRepository.findByRestaurant(restaurant);
        if(isExisting.isPresent()) {
            throw new ApiException(RestaurantMessage.RECOMMEND_RESTAURANT_CONFLICT);
        }
    }

    private RestaurantEntity findRestaurant(final String kakaoSubId) {
        Optional<RestaurantEntity> isExisting = restaurantRepository.findByKakaoSubId(kakaoSubId);
        if(isExisting.isEmpty()) {
            throw new ApiException(RestaurantMessage.RESTAURANT_LOCATION_NOT_FOUND);
        }

        return isExisting.get();
    }

    public FindDetailRestaurantResponse findDetailRestaurant(Long recommendRestaurantId) {
        Optional<RecommendRestaurantEntity> isExisting = recommendRestaurantRepository.findById(recommendRestaurantId);
        if(isExisting.isEmpty()) {
            throw new ApiException(RestaurantMessage.RECOMMEND_RESTAURANT_NOT_FOUND);
        }
        return isExisting.get().toResponse();
    }
}
