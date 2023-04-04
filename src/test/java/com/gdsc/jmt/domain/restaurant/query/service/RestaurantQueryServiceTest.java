package com.gdsc.jmt.domain.restaurant.query.service;

import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
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
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
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
            KakaoSearchResponse kakaoSearchResponse = makeMockKaKaoApiResponse();
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
            Optional<RestaurantEntity> isExisting = Optional.ofNullable(makeMockRestaurantEntity(kakaoSubId));
            Mockito.when(restaurantRepository.findByKakaoSubId(kakaoSubId))
                    .thenReturn(isExisting);

            // when then
            ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
                restaurantQueryService.checkRestaurantExisting(kakaoSubId);
            });
            Assertions.assertEquals(exception.getResponseMessage(), RestaurantMessage.RESTAURANT_LOCATION_CONFLICT);
        }

        @Test
        public void 등록되지_않은_맛집_위치정보_조회() {
            // given
            String kakaoSubId = "123456789";
            // when then
            Assertions.assertDoesNotThrow(() -> {
                    restaurantQueryService.checkRestaurantExisting(kakaoSubId);
            });
        }
    }


    private RestaurantEntity makeMockRestaurantEntity(String kakaoSubId) {
        try {
            String pointWKT = String.format("POINT(%s %s)", 1.2, 3.4);
            Point testLocation = (Point) new WKTReader().read(pointWKT);

            return RestaurantEntity.builder()
                    .kakaoSubId(kakaoSubId)
                    .name("마제소바 맛집")
                    .placeUrl("https://test맛집.com")
                    .category("양식")
                    .phone("010-1234-5678")
                    .address("서울 노원구")
                    .roadAddress("도로명 주소")
                    .location(testLocation)
                    .aggregateId("123456789")
                    .build();
        }
        catch (ParseException ex) {
            return null;
        }
    }


    private KakaoSearchResponse makeMockKaKaoApiResponse() {
        List<KakaoSearchDocument> kakaoSearchDocuments = new ArrayList<>();
        KakaoSearchDocument document = new KakaoSearchDocument(
                "마제소바",
                "0",
                "http://place.map.kakao.com/1574464357",
                "음식점 > 일식",
                "대구 동구 신천동 384-12",
                "대구 동구 동부로 158-7",
                "1574464357",
                "",
                "FD6",
                "음식점",
                "128.629555401333",
                "35.8768967936303");

        kakaoSearchDocuments.add(document);
        return new KakaoSearchResponse(null, kakaoSearchDocuments);
    }

}
