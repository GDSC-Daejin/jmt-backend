package com.gdsc.jmt.domain.restaurant.query.service;

import com.gdsc.jmt.domain.restaurant.query.dto.request.*;
import com.gdsc.jmt.domain.restaurant.query.dto.response.*;
import com.gdsc.jmt.domain.restaurant.query.dto.PageMeta;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.ReportReasonEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantReviewEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.calculate.RecommendRestaurantWithDistanceDTO;
import com.gdsc.jmt.domain.restaurant.query.repository.RecommendRestaurantRepository;
import com.gdsc.jmt.domain.restaurant.query.repository.ReportReasonRepository;
import com.gdsc.jmt.domain.restaurant.query.repository.RestaurantRepository;
import com.gdsc.jmt.domain.restaurant.query.repository.RestaurantReviewRepository;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocumentResponse;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchResponse;
import com.gdsc.jmt.domain.restaurant.util.RestaurantAPIUtil;
import com.gdsc.jmt.global.dto.PageResponse;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantQueryService {

    private final RestaurantAPIUtil restaurantAPIUtil;

    private final RestaurantRepository restaurantRepository;

    // 혹시 해당 조회 기능이 늘어나는 경우 서비스 분리 필요
    private final RecommendRestaurantRepository recommendRestaurantRepository;
    private final ReportReasonRepository reportReasonRepository;

    private final RestaurantReviewRepository restaurantReviewRepository;

    private final RestaurantFilterService restaurantFilterService;
    private final RestaurantDynamicSearchService restaurantDynamicSearchService;


    public List<KakaoSearchDocumentResponse> findRestaurantLocationList(final FindRestaurantLocationListRequest findRestaurantLocationListRequest) {
        KakaoSearchResponse response = restaurantAPIUtil.findRestaurantLocation(findRestaurantLocationListRequest);
        return response.getDocuments().stream().map(KakaoSearchDocument::convertResponse).toList();
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

    public FindRestaurantResponse search(final RestaurantSearchRequest request, Pageable pageable) {
        Page<RecommendRestaurantEntity> recommendRestaurantPage;

        Specification<RecommendRestaurantEntity> specification = restaurantDynamicSearchService.searchKeywordRestaurant(request);
        recommendRestaurantPage = recommendRestaurantRepository.findAll(specification, pageable);

        PageResponse pageResponse = new PageResponse(recommendRestaurantPage);
        return new FindRestaurantResponse(
                recommendRestaurantPage.getContent().stream().map(RecommendRestaurantEntity::convertToFindItems).toList(),
                pageResponse
        );
    }

    @Transactional(readOnly = true)
    public FindRestaurantResponse searchInMap(RestaurantSearchMapRequest request, Pageable pageable) {
        List<Long> categoryIds = restaurantFilterService.findCategoryIdsByFilter(request.filter());
        Page<RecommendRestaurantEntity> nearlyRestaurants = findRestaurantInRadius(request, categoryIds, request.filter().isCanDrinkLiquor() ,pageable);

        PageResponse pageResponse = new PageResponse(nearlyRestaurants);
        return new FindRestaurantResponse(
                nearlyRestaurants.getContent().stream().map(RecommendRestaurantEntity::convertToFindItems).toList(),
                pageResponse
        );
    }

    private Page<RecommendRestaurantEntity> findRestaurantInRadius(RestaurantSearchMapRequest request, List<Long> categoryIds, Boolean isCanDrinkLiquor, Pageable pageable) {
        Specification<RecommendRestaurantEntity> specification = restaurantDynamicSearchService.searchMapRestaurant(request, categoryIds);
        return recommendRestaurantRepository.findAll(specification, pageable);
    }

    private RecommendRestaurantEntity findRecommendRestaurantByRestaurant(RestaurantEntity restaurant) {
        Optional<RecommendRestaurantEntity> result = recommendRestaurantRepository.findByRestaurant(restaurant);
        return result.orElse(null);
    }

    public FindRestaurantResponse searchInUserId(Long userId, RestaurantSearchInUserIdRequest request, Pageable pageable) {
        Page<RecommendRestaurantWithDistanceDTO> recommendRestaurantPage = findRecommendRestaurantByUserId(userId, request, pageable);
        PageResponse pageResponse = new PageResponse(recommendRestaurantPage);
        return new FindRestaurantResponse(
                recommendRestaurantPage.getContent().stream().map(RecommendRestaurantWithDistanceDTO::convertToFindItems).toList(),
                pageResponse
        );
    }

    public List<FindReportReasonResponse> findAllReportReason() {
        List<ReportReasonEntity> reportReasonEntities = reportReasonRepository.findAll();
        return reportReasonEntities.stream().map(ReportReasonEntity::convertResponse).toList();
    }

    private Page<RecommendRestaurantWithDistanceDTO> findRecommendRestaurantByUserId(Long userId, RestaurantSearchInUserIdRequest request, Pageable pageable) {
        String userLocation = "POINT(" + request.userLocation().x() + " " + request.userLocation().y() + ")";
        return recommendRestaurantRepository.findByUserId(userId, userLocation, pageable);
    }

    public FindRestaurantReviewResponse findAllReview(Long recommendRestaurantId, Pageable pageable) {
        Page<RestaurantReviewEntity> result = restaurantReviewRepository.findByRecommendRestaurantId(recommendRestaurantId, pageable);

        PageResponse pageResponse = new PageResponse(result);
        return new FindRestaurantReviewResponse(
                result.getContent().stream().map(RestaurantReviewEntity::toResponse).toList(),
                pageResponse
        );
    }
}
