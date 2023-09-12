package com.gdsc.jmt.domain.api.query.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gdsc.jmt.domain.api.query.dto.request.NaverLocationConvertRequest;
import com.gdsc.jmt.domain.api.query.service.ApiService;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.messege.ApiMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "API 지원 컨트롤러")
@FirstVersionRestController
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;

    @PostMapping("/naver/location/convert")
    public JMTApiResponse<?> naverApiLocationCovert(@RequestBody NaverLocationConvertRequest request) throws JsonProcessingException {
        Object naverResponse = apiService.naverLocationConvert(request);
        return JMTApiResponse.createResponseWithMessage(naverResponse, ApiMessage.NAVER_LOCATION_CONVERT_SUCCESS);
    }
}
