package com.gdsc.jmt.domain.restaurant.command.aggregate;

import com.gdsc.jmt.domain.restaurant.command.CreateRecommendRestaurantCommand;
import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRecommendRestaurantEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Getter
@RequiredArgsConstructor
public class RecommendRestaurantAggregate {
    @AggregateIdentifier
    private String id;

    private String introduce;
    private Long categoryId;

    private Long userId;

    private Boolean canDrinkLiquor;

    private String goWellWithLiquor;

    private String recommendMenu;

    @CommandHandler
    public RecommendRestaurantAggregate(CreateRecommendRestaurantCommand command) {
        AggregateLifecycle.apply(new CreateRecommendRestaurantEvent(
                command.getId(),
                command.getCreateRecommendRestaurantRequest()
        ));
    }

    @EventSourcingHandler
    public void createdRecommendRestaurant(CreateRecommendRestaurantEvent event) {
        CreateRecommendRestaurantRequest createdRequest = event.getCreateRecommendRestaurantRequest();
        this.id = event.getId();
        this.introduce = createdRequest.getIntroduce();
        this.categoryId = createdRequest.getCategoryId();
        this.canDrinkLiquor = createdRequest.getCanDrinkLiquor();
        this.goWellWithLiquor = createdRequest.getGoWellWithLiquor();
        this.recommendMenu = createdRequest.getRecommendMenu();
    }
}
