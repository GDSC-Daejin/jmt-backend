package com.gdsc.jmt.domain.restaurant.query.controller;

import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRestaurantReviewRequest;
import com.gdsc.jmt.domain.restaurant.query.controller.springdocs.FindAllRestaurantSpringDocs;
import com.gdsc.jmt.domain.restaurant.query.controller.springdocs.FindRestaurantsByUserIdSpringDocs;
import com.gdsc.jmt.domain.restaurant.query.dto.request.*;
import com.gdsc.jmt.domain.restaurant.query.dto.response.*;
import com.gdsc.jmt.domain.restaurant.query.controller.springdocs.CheckRecommendRestaurantExistingSpringDocs;
import com.gdsc.jmt.domain.restaurant.query.controller.springdocs.FindRestaurantLocationSpringDocs;
import com.gdsc.jmt.domain.restaurant.query.service.RestaurantFilterService;
import com.gdsc.jmt.domain.restaurant.query.service.RestaurantQueryService;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocumentResponse;
import com.gdsc.jmt.domain.user.command.controller.springdocs.FindDetailRestaurantSpringDocs;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.jwt.dto.UserInfo;
import com.gdsc.jmt.global.messege.RestaurantMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FirstVersionRestController
@RequiredArgsConstructor
@Tag(name = "맛집 조회 컨트롤러")
public class RestaurantQueryController {
    private final RestaurantQueryService restaurantQueryService;
    private final RestaurantFilterService restaurantFilterService;


    @GetMapping("/restaurant")
    @FindAllRestaurantSpringDocs
    @PageableAsQueryParam
    public JMTApiResponse<FindAllRestaurantResponse> findAllRestaurant(@PageableDefault @Parameter(hidden = true) Pageable pageable) {
        FindAllRestaurantResponse result = restaurantQueryService.findAll(pageable);
        return JMTApiResponse.createResponseWithMessage(result, RestaurantMessage.RESTAURANT_FIND_ALL);
    }

    @GetMapping("/restaurant/location")
    @FindRestaurantLocationSpringDocs
    public JMTApiResponse<List<KakaoSearchDocumentResponse>> findRestaurantLocationList(@ModelAttribute FindRestaurantLocationListRequest findRestaurantLocationListRequest) {
        List<KakaoSearchDocumentResponse> restaurants = restaurantQueryService.findRestaurantLocationList(findRestaurantLocationListRequest);
        return JMTApiResponse.createResponseWithMessage(restaurants, RestaurantMessage.RESTAURANT_LOCATION_FIND);
    }

    @GetMapping("/restaurant/registration/{kakaoSubId}")
    @CheckRecommendRestaurantExistingSpringDocs
    public JMTApiResponse<?> checkRecommendRestaurantExisting(@PathVariable String kakaoSubId) {
        restaurantQueryService.checkRecommendRestaurantExisting(kakaoSubId);
        return JMTApiResponse.createResponseWithMessage(true, RestaurantMessage.RECOMMEND_RESTAURANT_REGISTERABLE);
    }

    @PostMapping("restaurant/{recommendRestaurantId}")
    @FindDetailRestaurantSpringDocs
    public JMTApiResponse<FindDetailRestaurantItem> getDetailRestaurant(@PathVariable Long recommendRestaurantId, @RequestBody FindDetailRestaurantRequest request) {
        FindDetailRestaurantItem response = restaurantQueryService.findDetailRestaurant(recommendRestaurantId, request);
        return JMTApiResponse.createResponseWithMessage(response, RestaurantMessage.DETAIL_RESTAURANT_FIND_SUCCESS);
    }

    @PostMapping("restaurant/search")
    public JMTApiResponse<FindRestaurantResponse> restaurantSearch(@RequestBody RestaurantSearchRequest request,
                                                                   @PageableDefault @Parameter(hidden = true) Pageable pageable,
                                                                   @AuthenticationPrincipal UserInfo userInfo) {
        FindRestaurantResponse response = restaurantQueryService.search(request, pageable, userInfo);
        return JMTApiResponse.createResponseWithMessage(response, RestaurantMessage.RESTAURANT_SEARCH_FIND);
    }

    @PostMapping("restaurant/search/map")
    @PageableAsQueryParam
    public JMTApiResponse<FindRestaurantResponse> restaurantSearchInMap(@RequestBody RestaurantSearchMapRequest request,
                                                                        @PageableDefault @Parameter(hidden = true) Pageable pageable) {
        FindRestaurantResponse restaurants = restaurantQueryService.searchInMap(request, pageable);
        return JMTApiResponse.createResponseWithMessage(restaurants, RestaurantMessage.RESTAURANT_SEARCH_FIND);
    }

    @PostMapping("restaurant/search/{userid}")
    @PageableAsQueryParam
    @FindRestaurantsByUserIdSpringDocs
    public JMTApiResponse<FindRestaurantResponse> restaurantSearchInUserId(@PathVariable("userid") Long userId,
            @RequestBody RestaurantSearchInUserIdRequest request,
            @PageableDefault @Parameter(hidden = true) Pageable pageable) {
        FindRestaurantResponse restaurants = restaurantQueryService.searchInUserId(userId, request, pageable);
        FindRestaurantResponse response = new FindRestaurantResponse(restaurantFilterService.applyFilter(request.filter(), restaurants.restaurants()),
                restaurants.page());
        return JMTApiResponse.createResponseWithMessage(response, RestaurantMessage.RESTAURANT_SEARCH_FIND);
    }

    @GetMapping(value = "/restaurant/reports")
    @Operation(summary = "맛집 신고 사유 코드 조회 API", description = "신고 사유 조회")
    public JMTApiResponse<List<FindReportReasonResponse>> reportRecommendRestaurant() {
        List<FindReportReasonResponse> reportReasonResponseList = restaurantQueryService.findAllReportReason();
        return JMTApiResponse.createResponseWithMessage(reportReasonResponseList, RestaurantMessage.FIND_ALL_REPORT_REASON);
    }

    @GetMapping(value = "/restaurant/{recommendRestaurantId}/review")
    @Operation(summary = "맛집 후기 조회 API", description = "맛집 후기 조회")
    public JMTApiResponse<FindRestaurantReviewResponse> restaurantReview(@PathVariable Long recommendRestaurantId,
                                              @PageableDefault @Parameter(hidden = true) Pageable pageable) {
        FindRestaurantReviewResponse response = restaurantQueryService.findAllReview(recommendRestaurantId, pageable);
        return JMTApiResponse.createResponseWithMessage(response, RestaurantMessage.RESTAURANT_REVIEW_FIND_ALL);
    }

    @PostMapping(value = "/restaurant/my/review")
    @Operation(summary = "나의 맛집 후기 조회 API", description = "내가 등록한 맛집 후기 조회")
    public JMTApiResponse<FindRestaurantReviewResponse> restaurantReview(@AuthenticationPrincipal UserInfo userInfo,
                                                                         @RequestBody FindMyReviewRequest request,
                                                                         @PageableDefault @Parameter(hidden = true) Pageable pageable) {
        FindRestaurantReviewResponse response = restaurantQueryService.findMyReview(request, userInfo, pageable);
        return JMTApiResponse.createResponseWithMessage(response, RestaurantMessage.RESTAURANT_MY_REVIEW_FIND_ALL);
    }

    @PostMapping(value = "/restaurant/search/outbound")
    @Operation(summary = "다른 그룹 맛집 조회 API", description = "다른 그룹 맛집 조회 API (랜덤 3개 제한)")
    public JMTApiResponse<FindRestaurantResponse> searchFromOtherGroup(@RequestBody RestaurantSearchFromOtherGroupRequest request) {
        FindRestaurantResponse response = restaurantQueryService.searchFromOtherGroup(request);
        return JMTApiResponse.createResponseWithMessage(response, RestaurantMessage.RESTAURANT_SEARCH_FIND_FROM_OTHER_GROUP);
    }
}
