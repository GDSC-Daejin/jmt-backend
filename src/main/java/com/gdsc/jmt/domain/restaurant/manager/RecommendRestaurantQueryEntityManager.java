package com.gdsc.jmt.domain.restaurant.manager;

import com.gdsc.jmt.domain.category.query.entity.CategoryEntity;
import com.gdsc.jmt.domain.category.query.repository.CategoryRepository;
import com.gdsc.jmt.domain.restaurant.command.aggregate.RecommendRestaurantAggregate;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRecommendRestaurantEvent;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity.RecommendRestaurantEntityBuilder;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantPhotoEntity;
import com.gdsc.jmt.domain.restaurant.query.repository.RecommendRestaurantRepository;
import com.gdsc.jmt.domain.restaurant.query.repository.RestaurantRepository;
import com.gdsc.jmt.global.event.BaseEvent;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.DefaultMessage;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecommendRestaurantQueryEntityManager {
    private final EventSourcingRepository<RecommendRestaurantAggregate> recommendRestaurantAggregateEventSourcingRepository;
    private final RecommendRestaurantRepository recommendRestaurantRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    @EventSourcingHandler
    public void on(CreateRecommendRestaurantEvent event) {
        RecommendRestaurantEntityBuilder recommendRestaurantEntityBuilder = validateCreation(event.getRestaurantName(), event.getRecommendRestaurantRequest().getCategoryId());
        persistRecommendRestaurant(createRecommendRestaurant(getRecommendRestaurantFromEvent(event), recommendRestaurantEntityBuilder));
    }

    private RecommendRestaurantAggregate getRecommendRestaurantFromEvent(BaseEvent<String> event) {
        return recommendRestaurantAggregateEventSourcingRepository.load(event.getId())
                .getWrappedAggregate()
                .getAggregateRoot();
    }

    private RecommendRestaurantEntityBuilder validateCreation(final String restaurantName, final Long categoryId) {
        RestaurantEntity restaurant = validateRestaurant(restaurantName);
        CategoryEntity category = validateCategory(categoryId);
        validateConflict(restaurant);

        return RecommendRestaurantEntity.builder()
                .restaurant(restaurant)
                .category(category);
    }

    private RestaurantEntity validateRestaurant(final String restaurantName) {
        // TODO : 네이버 API 연동전 로직
        Optional<RestaurantEntity> restaurant = restaurantRepository.findByName(restaurantName);
        if(restaurant.isEmpty())
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        return  restaurant.get();
    }

    private CategoryEntity validateCategory(final Long categoryId) {
        Optional<CategoryEntity> category = categoryRepository.findById(categoryId);
        if(category.isEmpty())
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        return category.get();
    }

    private void validateConflict(RestaurantEntity restaurant) {
        Optional<RecommendRestaurantEntity> recommendRestaurant = recommendRestaurantRepository.findByRestaurant(restaurant);
        if(recommendRestaurant.isPresent())
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
    }

    private RecommendRestaurantEntity createRecommendRestaurant(RecommendRestaurantAggregate recommendRestaurantAggregate, RecommendRestaurantEntityBuilder recommendRestaurantEntityBuilder) {
        // TODO : 이거는 실제 이미지 올리는 서버가 있으면 처리하기..
        List<RestaurantPhotoEntity> test = new ArrayList<>();

        return recommendRestaurantEntityBuilder
                .introduce(recommendRestaurantAggregate.getIntroduce())
                .pictures(test)
                .canDrinkLiquor(recommendRestaurantAggregate.getCanDrinkLiquor())
                .goWellWithLiquor(recommendRestaurantAggregate.getGoWellWithLiquor())
                .recommendMenu(recommendRestaurantAggregate.getRecommendMenu())
                .aggregateId(recommendRestaurantAggregate.getId())
                .build();
    }

    private void persistRecommendRestaurant(RecommendRestaurantEntity recommendRestaurant) {
        recommendRestaurantRepository.save(recommendRestaurant);
    }
}
