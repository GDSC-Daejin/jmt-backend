package com.gdsc.jmt.domain.restaurant.manager;

import com.gdsc.jmt.domain.restaurant.MockRestaurantMaker;
import com.gdsc.jmt.domain.restaurant.MockKakaoMaker;
import com.gdsc.jmt.domain.restaurant.command.aggregate.RestaurantAggregate;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRestaurantEvent;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.repository.RestaurantRepository;
import org.axonframework.eventsourcing.EventSourcingRepository;
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

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Import({RestaurantQueryEntityManager.class})
public class RestaurantManagerTest {

    @MockBean
    EventSourcingRepository<RestaurantAggregate> restaurantAggregateEventSourcingRepository;

    @MockBean
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantQueryEntityManager restaurantQueryEntityManager;


    @Nested
    @DisplayName("RestaurantQueryEntityManager Handler Test")
    class RestaurantQueryEntityManagerTest {
            @Test
            @DisplayName("Restaurant 생성 이벤트 핸들러 성공")
            public void createdRestaurantTest() {
                // given
                String aggregateId = "abcd123456789";
                String kakaoSubId = "123456789";
                Mockito.when(restaurantRepository.findByKakaoSubId(kakaoSubId))
                        .thenReturn(Optional.empty());

                // 1.restaurantAggregateEventSourcingRepository Mock 처리
                // 이슈 : 체인닝 전체를 Mock 해서 하고싶은데 막상 실행은 하나의 함수씩 실행되서 에러 발생
                // 2. restaurantQueryEntityManager.getRestaurantFromEvent() 함수 Mock 처리
                // 이슈 : private 함수 + restaurantQueryEntityManager 자체는 의존성 주입받아서 하는중

//                Mockito.when(restaurantAggregateEventSourcingRepository.load(aggregateId)
//                        .getWrappedAggregate()
//                        .getAggregateRoot())
//                        .thenReturn(restaurantAggregate);

                RestaurantEntity restaurant = MockRestaurantMaker.makeMockRestaurantEntity(kakaoSubId);
                assert restaurant != null;
                Mockito.when(restaurantRepository.save(restaurant))
                        .thenReturn(restaurant);
                // when then
                Assertions.assertDoesNotThrow(() -> restaurantQueryEntityManager.createdRestaurant(makeMockCreateRestaurantEvent(aggregateId)));
            }
    }


    private CreateRestaurantEvent makeMockCreateRestaurantEvent(String aggregateId) {
        return new CreateRestaurantEvent(
                aggregateId,
                MockKakaoMaker.makeMockKakaoSearchDocument()
        );
    }

    private RestaurantAggregate mackMockRestaurantAggregate(String aggregateId) {
        RestaurantAggregate restaurantAggregate = new RestaurantAggregate();
        restaurantAggregate.on(makeMockCreateRestaurantEvent(aggregateId));
        return restaurantAggregate;
    }
}
