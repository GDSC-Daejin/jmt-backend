package com.gdsc.jmt.domain.restaurant.manager;

import com.gdsc.jmt.domain.category.query.entity.CategoryEntity;
import com.gdsc.jmt.domain.category.query.repository.CategoryRepository;
import com.gdsc.jmt.domain.restaurant.command.aggregate.RecommendRestaurantAggregate;
import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRecommendRestaurantEvent;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity.RecommendRestaurantEntityBuilder;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantEntity;
import com.gdsc.jmt.domain.restaurant.query.entity.RestaurantPhotoEntity;
import com.gdsc.jmt.domain.restaurant.query.repository.RecommendRestaurantRepository;
import com.gdsc.jmt.domain.restaurant.query.repository.RestaurantRepository;
import com.gdsc.jmt.domain.user.query.entity.UserEntity;
import com.gdsc.jmt.domain.user.query.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @EventSourcingHandler
    public void createdRecommendRestaurant(CreateRecommendRestaurantEvent event) {
        CreateRecommendRestaurantRequest createRecommendRestaurantRequest = event.getCreateRecommendRestaurantRequest();
        RecommendRestaurantEntityBuilder recommendRestaurantEntityBuilder = validateCreation(createRecommendRestaurantRequest.getKakaoSubId(), createRecommendRestaurantRequest.getCategoryId(), event.getUserAggregateId());
        persistRecommendRestaurant(createRecommendRestaurant(getRecommendRestaurantFromEvent(event), recommendRestaurantEntityBuilder));
    }

    private RecommendRestaurantAggregate getRecommendRestaurantFromEvent(BaseEvent<String> event) {
        return recommendRestaurantAggregateEventSourcingRepository.load(event.getId())
                .getWrappedAggregate()
                .getAggregateRoot();
    }

    private RecommendRestaurantEntityBuilder validateCreation(final String restaurantKakaSubId, final Long categoryId, final String userAggregateId) {
        RestaurantEntity restaurant = validateRestaurant(restaurantKakaSubId);
        CategoryEntity category = validateCategory(categoryId);
        UserEntity user = validateUser(userAggregateId);
        validateConflict(restaurant);

        return RecommendRestaurantEntity.builder()
                .user(user)
                .restaurant(restaurant)
                .category(category);
    }

    private UserEntity validateUser(String userAggregateId) {
        Optional<UserEntity> result = userRepository.findByUserAggregateId(userAggregateId);
        if(result.isEmpty())
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        return  result.get();
    }

    private RestaurantEntity validateRestaurant(final String restaurantKakaSubId) {
        Optional<RestaurantEntity> restaurant = restaurantRepository.findByKakaoSubId(restaurantKakaSubId);
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
