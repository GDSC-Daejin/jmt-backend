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

    public void searchInMap(RestaurantSearchMapRequest request, Pageable pageable) throws ParseException {
        String pointWKT = String.format("POINT(%s %s)", request.x(), request.y());
        Point userLocation = (Point) new WKTReader().read(pointWKT);
        List<RestaurantEntity> nearlyRestaurants = findRestaurantInRadius(userLocation, (double) request.radius());
        // 찾은 맛집 위치 정보에서 실제로 사용자가 등록한 맛집으로 보여주는 로직 필요
    }

    private List<RestaurantEntity> findRestaurantInRadius(Point userLocation, double radiusInMeters) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinates = new Coordinate[5];
        double distanceInDegrees = radiusInMeters / 111320.0; // 1도 당 약 111320m
        coordinates[0] = new Coordinate(userLocation.getX() - distanceInDegrees, userLocation.getY() - distanceInDegrees);
        coordinates[1] = new Coordinate(userLocation.getX() + distanceInDegrees, userLocation.getY() - distanceInDegrees);
        coordinates[2] = new Coordinate(userLocation.getX() + distanceInDegrees, userLocation.getY() + distanceInDegrees);
        coordinates[3] = new Coordinate(userLocation.getX() - distanceInDegrees, userLocation.getY() + distanceInDegrees);
        coordinates[4] = coordinates[0];

        Polygon searchRectangle = geometryFactory.createPolygon(coordinates);
        // 사각형 내에 포함되는 데이터 조회
        return restaurantRepository.findByLocationWithin(searchRectangle);
    }


}
