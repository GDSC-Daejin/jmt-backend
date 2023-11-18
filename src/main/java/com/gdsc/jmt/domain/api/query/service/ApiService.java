package com.gdsc.jmt.domain.api.query.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gdsc.jmt.domain.api.query.dto.request.NaverLocationConvertRequest;
import com.gdsc.jmt.domain.api.util.APIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final APIUtil apiUtil;

    public Object naverLocationConvert(NaverLocationConvertRequest request) throws JsonProcessingException {
        return apiUtil.naverLocationConvert(request);
    }
}
