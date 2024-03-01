package com.gdsc.jmt.domain.restaurant.command.controller;

import com.gdsc.jmt.domain.restaurant.command.controller.springdocs.CreateRecommendRestaurantSpringDocs;
import com.gdsc.jmt.domain.restaurant.command.controller.springdocs.CreateRestaurantLocationSpringDocs;
import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequestFromClient;
import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRestaurantReviewRequest;
import com.gdsc.jmt.domain.restaurant.command.dto.request.ReportRecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.dto.request.UpdateRecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.dto.response.CreatedRestaurantResponse;
import com.gdsc.jmt.domain.restaurant.command.service.RestaurantService;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocumentRequest;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.jwt.dto.UserInfo;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@FirstVersionRestController
@RequiredArgsConstructor
@Tag(name = "맛집 등록 컨트롤러")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping(value = "/restaurant/location")
    @CreateRestaurantLocationSpringDocs
    @ResponseStatus(HttpStatus.CREATED)
    public JMTApiResponse<Long> createRestaurantLocation(@RequestBody KakaoSearchDocumentRequest kakaoSearchDocumentRequest) {
        Long restaurantLocationId = restaurantService.createRestaurantLocation(kakaoSearchDocumentRequest);
        return JMTApiResponse.createResponseWithMessage(restaurantLocationId, RestaurantMessage.RESTAURANT_LOCATION_CREATED);
    }

    @PostMapping(value = "/restaurant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CreateRecommendRestaurantSpringDocs
    @ResponseStatus(HttpStatus.CREATED)
    public JMTApiResponse<CreatedRestaurantResponse> createRecommendRestaurant(@ModelAttribute CreateRecommendRestaurantRequestFromClient request, @AuthenticationPrincipal UserInfo user) {
        CreatedRestaurantResponse response = restaurantService.createRecommendRestaurant(request, user.getEmail());
        return JMTApiResponse.createResponseWithMessage(response, RestaurantMessage.RESTAURANT_CREATED);
    }

    @PutMapping(value = "/restaurant")
    @Operation(summary = "맛집 수정 API", description = "맛집 정보 업데이트 (사진 불가)")
    public JMTApiResponse<?> updateRecommendRestaurant(@RequestBody UpdateRecommendRestaurantRequest request, @AuthenticationPrincipal UserInfo user) {
        restaurantService.updateRecommendRestaurant(request, user.getEmail());
        return JMTApiResponse.createResponseWithMessage(null, RestaurantMessage.RECOMMEND_RESTAURANT_UPDATED);
    }

    @DeleteMapping(value = "/restaurant/{id}")
    @Operation(summary = "맛집 삭제 API", description = "사용자가 등록한 맛집 정보 삭제")
    public JMTApiResponse<?> removeRecommendRestaurant(@PathVariable Long id, @AuthenticationPrincipal UserInfo user) {
        restaurantService.removeRecommendRestaurant(id, user.getEmail());
        return JMTApiResponse.createResponseWithMessage(null, RestaurantMessage.RECOMMEND_RESTAURANT_DELETED);
    }

    @PostMapping(value = "/restaurant/{id}/report")
    @Operation(summary = "맛집 신고 API", description = "맛집 정보 신고")
    public JMTApiResponse<?> reportRecommendRestaurant(@PathVariable Long id, @AuthenticationPrincipal UserInfo reporter, @RequestBody ReportRecommendRestaurantRequest request) {
        restaurantService.reportRecommendRestaurant(id, reporter, request);
        return JMTApiResponse.createResponseWithMessage(null, RestaurantMessage.RECOMMEND_RESTAURANT_REPORTED);
    }


    @PostMapping(value = "/restaurant/{recommendRestaurantId}/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "맛집 후기 작성 API", description = "맛집 후기 작성")
    @ResponseStatus(HttpStatus.CREATED)
    public JMTApiResponse<?> restaurantReview(@PathVariable Long recommendRestaurantId,
                                              @AuthenticationPrincipal UserInfo user,
                                              @ModelAttribute CreateRestaurantReviewRequest request) {
        restaurantService.createRestaurantReview(recommendRestaurantId, user, request);
        return JMTApiResponse.createResponseWithMessage(null, RestaurantMessage.RESTAURANT_REVIEW_CREATED);
    }
}
