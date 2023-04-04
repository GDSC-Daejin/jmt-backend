package com.gdsc.jmt.domain.restaurant.command;

import com.gdsc.jmt.domain.restaurant.MockKakaoMaker;
import com.gdsc.jmt.domain.restaurant.command.aggregate.RecommendRestaurantAggregate;
import com.gdsc.jmt.domain.restaurant.command.aggregate.RestaurantAggregate;
import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRecommendRestaurantEvent;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRestaurantEvent;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class RestaurantDomainCommandTest {

    private FixtureConfiguration<RestaurantAggregate> restaurantFixture;
    private FixtureConfiguration<RecommendRestaurantAggregate> recommendRestaurantFixture;

    @BeforeEach
    public void setUp() {
        restaurantFixture = new AggregateTestFixture<>(RestaurantAggregate.class);
        recommendRestaurantFixture = new AggregateTestFixture<>(RecommendRestaurantAggregate.class);
    }

    @Nested
    @DisplayName("RestaurantAggregate Command Test")
    class RestaurantAggregateCommandTest {
        @Test
        public void 맛집_위치정보_Create_Command_발행() {
            // given
            String aggregateId = "123-456-789";
            KakaoSearchDocument kakaoSearchDocument = MockKakaoMaker.makeMockKakaoSearchDocument();

            // when then
            restaurantFixture.given()
                    .when(new CreateRestaurantCommand(aggregateId, kakaoSearchDocument))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(new CreateRestaurantEvent(aggregateId, kakaoSearchDocument));
        }
    }

    @Nested
    @DisplayName("RecommendRestaurantAggregate Command Test")
    class RecommendRestaurantAggregateCommandTest {
        @Test
        public void 사용자_맛집_정보_Create_Command_발행() {
            // given
            String aggregateId = "123-456-789";
            CreateRecommendRestaurantRequest createRecommendRestaurantRequest = makeMockCreateRecommendRestaurantRequest();
            // when then
            recommendRestaurantFixture.given()
                    .when(new CreateRecommendRestaurantCommand(aggregateId, createRecommendRestaurantRequest))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(new CreateRecommendRestaurantEvent(aggregateId, createRecommendRestaurantRequest));
        }
    }

    private CreateRecommendRestaurantRequest makeMockCreateRecommendRestaurantRequest() {
        CreateRecommendRestaurantRequest request = new CreateRecommendRestaurantRequest();
        request.setName("마제소바 맛집");
        request.setIntroduce("마제소바");
        request.setCategoryId(1L);
        request.setCanDrinkLiquor(true);
        request.setGoWellWithLiquor("위스키");
        request.setRecommendMenu("#마제소바#라멘");
        request.setKakaoSubId("1574464357");
        return request;
    }
}
