package com.gdsc.jmt.domain.restaurant.query.service;

import com.gdsc.jmt.domain.restaurant.query.dto.FindAllRestaurantResponse;
import com.gdsc.jmt.domain.restaurant.query.dto.RestaurantSearchMapRequest;
import com.gdsc.jmt.domain.restaurant.query.dto.response.FindRestaurantItems;
import com.gdsc.jmt.domain.restaurant.query.dto.PageMeta;
import com.gdsc.jmt.domain.restaurant.query.dto.FindRestaurantLocationListRequest;
import com.gdsc.jmt.domain.restaurant.query.dto.response.FindDetailRestaurantItem;
import com.gdsc.jmt.domain.restaurant.query.dto.response.FindRestaurantResponse;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.repository.RecommendRestaurantRepository;
import com.gdsc.jmt.domain.restaurant.query.repository.RestaurantRepository;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchResponse;
import com.gdsc.jmt.domain.restaurant.util.RestaurantAPIUtil;
import com.gdsc.jmt.global.dto.PageResponse;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        if(restaurant == null) {
            return;
        }
        Optional<RecommendRestaurantEntity> isExisting = recommendRestaurantRepository.findByRestaurant(restaurant);
        if(isExisting.isPresent()) {
            throw new ApiException(RestaurantMessage.RECOMMEND_RESTAURANT_CONFLICT);
        }
    }

    private RestaurantEntity findRestaurant(final String kakaoSubId) {
        Optional<RestaurantEntity> isExisting = restaurantRepository.findByKakaoSubId(kakaoSubId);
//        if(isExisting.isEmpty()) {
//            throw new ApiException(RestaurantMessage.RESTAURANT_LOCATION_NOT_FOUND);
//        }
        return isExisting.orElse(null);
    }

    public FindDetailRestaurantItem findDetailRestaurant(Long recommendRestaurantId) {
        Optional<RecommendRestaurantEntity> isExisting = recommendRestaurantRepository.findById(recommendRestaurantId);
        if(isExisting.isEmpty()) {
            throw new ApiException(RestaurantMessage.RECOMMEND_RESTAURANT_NOT_FOUND);
        }
        return isExisting.get().toResponse();
    }
        
    public FindAllRestaurantResponse findAll(final Pageable pageable) {
        Page<RecommendRestaurantEntity> recommendRestaurantPage = recommendRestaurantRepository.findAll(pageable);

        List<FindRestaurantItems> restaurants = recommendRestaurantPage.getContent()
                .stream().map(RecommendRestaurantEntity::convertToFindItems)
                .toList();

        Pageable pageInfo = recommendRestaurantPage.getPageable();
        PageMeta pageMeta = new PageMeta(
                pageInfo.getPageNumber() + 1,
                pageInfo.getPageSize(),
                recommendRestaurantPage.getTotalPages()
        );
        return new FindAllRestaurantResponse(restaurants , pageMeta);
    }

    public FindRestaurantResponse search(final String keyword, Pageable pageable) {
        Page<RecommendRestaurantEntity> recommendRestaurantPage = recommendRestaurantRepository.findSearch(keyword, pageable);

        PageResponse pageResponse = new PageResponse(recommendRestaurantPage);
        return new FindRestaurantResponse(
                recommendRestaurantPage.getContent().stream().map(RecommendRestaurantEntity::convertToFindItems).toList(),
                pageResponse
        );
    }

    @Transactional(readOnly = true)
    public List<FindRestaurantItems> searchInMap(RestaurantSearchMapRequest request) {
        Point userLocation = null;
        try {
            String pointWKT = String.format("POINT(%s %s)", request.x(), request.y());
            userLocation = (Point) new WKTReader().read(pointWKT);
        }
        catch (ParseException e) {
            throw new ApiException(RestaurantMessage.LOCATION_PARSE_FAIL);
        }

        List<RestaurantEntity> nearlyRestaurants = findRestaurantInRadius(userLocation, request.radius());
        // TODO : 이거... 일단 이렇게 하긴했는데... 성능적으로 좋지는 않을 듯.. 차라리 RecommendRestaurant 엔티티에 위치정보 넣는게 훨~~씬 나을듯
        List<RecommendRestaurantEntity> recommendRestaurantEntities = new ArrayList<>();
        for(RestaurantEntity restaurant : nearlyRestaurants) {
            RecommendRestaurantEntity recommendRestaurant = findRecommendRestaurantByRestaurant(restaurant);
            if(recommendRestaurant != null) {
                recommendRestaurantEntities.add(recommendRestaurant);
            }
        }

        return recommendRestaurantEntities.stream().map(RecommendRestaurantEntity::convertToFindItems).toList();
    }

    private List<RestaurantEntity> findRestaurantInRadius(Point userLocation, Integer radiusInMeters) {
        // 사각형 내에 포함되는 데이터 조회
        return restaurantRepository.findByLocationWithinDistance(userLocation.getX(), userLocation.getY(), radiusInMeters);
    }

    private RecommendRestaurantEntity findRecommendRestaurantByRestaurant(RestaurantEntity restaurant) {
        Optional<RecommendRestaurantEntity> result = recommendRestaurantRepository.findByRestaurant(restaurant);
        return result.orElse(null);
    }

    public FindRestaurantResponse searchInUserId(Long userId, Pageable pageable) {
        Page<RecommendRestaurantEntity> recommendRestaurantPage = findRecommendRestaurantByUserId(userId, pageable);
        PageResponse pageResponse = new PageResponse(recommendRestaurantPage);
        return new FindRestaurantResponse(
                recommendRestaurantPage.getContent().stream().map(RecommendRestaurantEntity::convertToFindItems).toList(),
                pageResponse
        );
    }

    private Page<RecommendRestaurantEntity> findRecommendRestaurantByUserId(Long userId, Pageable pageable) {
        return recommendRestaurantRepository.findByUserId(userId, pageable);
    }
}
