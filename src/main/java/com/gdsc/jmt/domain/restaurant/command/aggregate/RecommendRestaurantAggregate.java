package com.gdsc.jmt.domain.restaurant.command.aggregate;

import com.gdsc.jmt.domain.restaurant.command.CreateRecommendRestaurantCommand;
import com.gdsc.jmt.domain.restaurant.command.dto.RecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRecommendRestaurantEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Aggregate
@RequiredArgsConstructor
public class RecommendRestaurantAggregate {
    @AggregateIdentifier
    private String id;

    private String introduce;
    private Long categoryId;

    // Aggregate에서 사진 정보를 MultipartFile로 넣어도 되려나요??
    private List<MultipartFile> pictures;

    private Boolean canDrinkLiquor;

    private String goWellWithLiquor;

    private String recommendMenu;

    @CommandHandler
    public RecommendRestaurantAggregate(CreateRecommendRestaurantCommand command) {
        AggregateLifecycle.apply(new CreateRecommendRestaurantEvent(
                command.getId(),
                command.getRecommendRestaurantRequest()
        ));
    }

    @EventSourcingHandler
    public void on(CreateRecommendRestaurantEvent event) {
        RecommendRestaurantRequest createRequest = event.getRecommendRestaurantRequest();
        this.id = event.getId();
        this.introduce = createRequest.introduce();
        this.categoryId = createRequest.categoryId();
        this.pictures = createRequest.pictures();
        this.canDrinkLiquor = createRequest.canDrinkLiquor();
        this.goWellWithLiquor = createRequest.goWellWithLiquor();
        this.recommendMenu = createRequest.recommendMenu();
    }
}
