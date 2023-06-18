package com.gdsc.jmt.domain.restaurant.command.service;

import com.gdsc.jmt.domain.restaurant.command.CreateRecommendRestaurantCommand;
import com.gdsc.jmt.domain.restaurant.command.CreateRestaurantCommand;
import com.gdsc.jmt.domain.restaurant.command.CreateRestaurantPhotoCommand;
import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequest;
import com.gdsc.jmt.domain.restaurant.command.dto.request.CreateRecommendRestaurantRequestFromClient;
import com.gdsc.jmt.domain.restaurant.command.dto.response.CreatedRestaurantResponse;
import com.gdsc.jmt.domain.restaurant.util.KakaoSearchDocument;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.RestaurantMessage;
import com.gdsc.jmt.global.service.S3FileService;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final CommandGateway commandGateway;

    private final S3FileService s3FileService;

    public CreatedRestaurantResponse createRecommendRestaurant(CreateRecommendRestaurantRequestFromClient request, String userAggregateId) {
        String aggregateId = UUID.randomUUID().toString();


        commandGateway.sendAndWait(new CreateRecommendRestaurantCommand(
                aggregateId,
                new CreateRecommendRestaurantRequest(request),
                userAggregateId
        ));

        uploadImages(request.getPictures());
        return new CreatedRestaurantResponse(request.getRestaurantLocationAggregateId(), aggregateId);
    }

    public String createRestaurantLocation(KakaoSearchDocument kakaoSearchDocumentRequest) {
        String aggregateId = UUID.randomUUID().toString();
        commandGateway.sendAndWait(new CreateRestaurantCommand(
                aggregateId,
                kakaoSearchDocumentRequest
        ));

        return aggregateId;
    }

    private void uploadImages(List<MultipartFile> images) {
        for(MultipartFile image : images) {
            try {
                String imageUrl = s3FileService.upload(image,"restaurantPhoto");
                String imageAggregateId = UUID.randomUUID().toString();

                commandGateway.sendAndWait(new CreateRestaurantPhotoCommand(
                        imageAggregateId,
                        imageUrl,
                        image.getSize()
                ));
            }
            catch (IOException e) {
                throw new ApiException(RestaurantMessage.RESTAURANT_IMAGE_UPLOAD_FAIL);
            }
        }
    }
}
