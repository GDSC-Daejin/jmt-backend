package com.gdsc.jmt.domain.restaurant.command.aggregate;

import com.gdsc.jmt.domain.restaurant.command.CreateRestaurantCommand;
import com.gdsc.jmt.domain.restaurant.command.event.CreateRestaurantEvent;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

@Aggregate
@Getter
@RequiredArgsConstructor
public class RestaurantAggregate {
    @AggregateIdentifier
    private String id;

    private String kakaoSubId;
    private String name;
    private String placeUrl;
    private String category;
    private String phone;
    private String address;
    private String roadAddress;

    private Point location;

    @CommandHandler
    public RestaurantAggregate(CreateRestaurantCommand createRestaurantCommand) {
        AggregateLifecycle.apply(new CreateRestaurantEvent(
                createRestaurantCommand.getId(),
                createRestaurantCommand.getKakaoSearchDocument()
        ));
    }

    @EventSourcingHandler
    public void on(CreateRestaurantEvent createRestaurantEvent) {
        KakaoSearchDocument kakaoSearchDocument = createRestaurantEvent.getKakaoSearchDocument();

        this.id = createRestaurantEvent.getId();
        this.kakaoSubId = kakaoSearchDocument.getId();
        this.name = kakaoSearchDocument.getPlace_name();
        this.placeUrl = kakaoSearchDocument.getPlace_url();
        this.category = kakaoSearchDocument.getCategory_name();
        this.phone = kakaoSearchDocument.getPhone();
        this.address = kakaoSearchDocument.getAddress_name();
        this.roadAddress = kakaoSearchDocument.getRoad_address_name();

        try {
            String pointWKT = String.format("POINT(%s %s)", kakaoSearchDocument.getX(), kakaoSearchDocument.getY());
            this.location = (Point) new WKTReader().read(pointWKT);
        } catch (ParseException ex) {
            throw new ApiException(RestaurantMessage.RESTAURANT_LOCATION_FAIL);
        }
    }
}
