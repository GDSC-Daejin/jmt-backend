package com.gdsc.jmt.global.dto;

import com.gdsc.jmt.global.messege.ResponseMessage;
import com.google.protobuf.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public class ApiResponse<T> {
    T data;
    String message;
    String code;

    public static <G> ApiResponse<G> createResponseWithMessage(G data, ResponseMessage responseMessage) {
        return new ApiResponse<>(data, responseMessage.getMessage(), responseMessage.toString());
    }
}
