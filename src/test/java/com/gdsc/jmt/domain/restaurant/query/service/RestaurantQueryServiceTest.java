package com.gdsc.jmt.domain.restaurant.query.service;

import com.gdsc.jmt.domain.restaurant.MockRestaurantMaker;
import com.gdsc.jmt.domain.restaurant.MockKakaoMaker;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.repository.RecommendRestaurantRepository;
import com.gdsc.jmt.domain.restaurant.query.repository.RestaurantRepository;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchResponse;
import com.gdsc.jmt.domain.restaurant.util.RestaurantAPIUtil;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Import({RestaurantQueryService.class})
public class RestaurantQueryServiceTest {

    @MockBean
    RestaurantRepository restaurantRepository;

    @MockBean
    RecommendRestaurantRepository recommendRestaurantRepository;

    @MockBean
    RestaurantAPIUtil restaurantAPIUtil;

    @Autowired
    RestaurantQueryService restaurantQueryService;


    @Nested
    @DisplayName("RestaurantQueryService findRestaurantLocationList Test")
    class findRestaurantLocationListTest {
        @Test
        public void 맛집_위치정보_리스트_조회() {
            // given
            String query = "마제소바"; int page = 1;
            KakaoSearchResponse kakaoSearchResponse = MockKakaoMaker.makeMockKaKaoApiResponse();
            Mockito.when(restaurantAPIUtil.findRestaurantLocation(query, page))
                    .thenReturn(kakaoSearchResponse);
            // when
            List<KakaoSearchDocument> result = restaurantQueryService.findRestaurantLocationList(query, page);

            //then
            Assertions.assertEquals(kakaoSearchResponse.getDocuments(), result);
        }

        @Test
        public void 맛집_위치정보_리스트_조회_결과없음() {
            // given
            String query = "마제소바"; int page = 1;
            KakaoSearchResponse kakaoSearchResponse = new KakaoSearchResponse(null, new ArrayList<>());
            Mockito.when(restaurantAPIUtil.findRestaurantLocation(query, page))
                    .thenReturn(kakaoSearchResponse);
            // when
            List<KakaoSearchDocument> result = restaurantQueryService.findRestaurantLocationList(query, page);
            //then
            Assertions.assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("RestaurantQueryService checkRestaurantExisting Test")
    class checkRestaurantExistingTest {

        @Test
        public void 이미_등록되어_있는맛집_위치정보_조회() {
            // given
            String kakaoSubId = "123456789";
            RestaurantEntity restaurant = MockRestaurantMaker.makeMockRestaurantEntity(kakaoSubId);
            Optional<RestaurantEntity> locationIsExisting = Optional.ofNullable(restaurant);
            Optional<RecommendRestaurantEntity> isExisting = Optional.ofNullable(MockRestaurantMaker.makcMockRecommendRestaurantEntity(restaurant));
            Mockito.when(restaurantRepository.findByKakaoSubId(kakaoSubId))
                    .thenReturn(locationIsExisting);
            Mockito.when(recommendRestaurantRepository.findByRestaurant(restaurant))
                    .thenReturn(isExisting);

            // when then
            ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
                restaurantQueryService.checkRecommendRestaurantExisting(kakaoSubId);
            });
            Assertions.assertEquals(exception.getResponseMessage(), RestaurantMessage.RECOMMEND_RESTAURANT_CONFLICT);
        }

        @Test
        public void 모든_것이_등록되지_않은_맛집_등록_유무_조회() {
            // given
            String kakaoSubId = "123456789";
            // when then
            ApiException exception = Assertions.assertThrows(ApiException.class , () -> {
                    restaurantQueryService.checkRecommendRestaurantExisting(kakaoSubId);
            });
            Assertions.assertEquals(exception.getResponseMessage(), RestaurantMessage.RESTAURANT_LOCATION_NOT_FOUND);
        }

        @Test
        public void 위치_정보만_등록되어있는_맛집_등록_유무_조회() {
            // given
            String kakaoSubId = "123456789";
            RestaurantEntity restaurant = MockRestaurantMaker.makeMockRestaurantEntity(kakaoSubId);
            Optional<RestaurantEntity> locationIsExisting = Optional.ofNullable(restaurant);
            Mockito.when(restaurantRepository.findByKakaoSubId(kakaoSubId))
                    .thenReturn(locationIsExisting);

            // when then
            Assertions.assertDoesNotThrow(() -> {
                restaurantQueryService.checkRecommendRestaurantExisting(kakaoSubId);
            });
        }
    }
}
