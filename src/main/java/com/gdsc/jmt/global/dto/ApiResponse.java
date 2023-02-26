package com.gdsc.jmt.global.dto;

import com.gdsc.jmt.global.messege.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    T data;
    String message;
    String code;

    public static <G> ApiResponse<G> createResponseWithMessage(G data, ResponseMessage responseMessage) {
        return new ApiResponse<>(data, responseMessage.getMessage(), responseMessage.toString());
    }
}
