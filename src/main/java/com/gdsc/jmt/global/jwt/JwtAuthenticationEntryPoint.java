package com.gdsc.jmt.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.jmt.global.dto.ApiResponse;
import com.gdsc.jmt.global.messege.DefaultMessage;
import com.gdsc.jmt.global.messege.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        ResponseMessage errorMessage = DefaultMessage.UNAUTHORIZED;
        ApiResponse apiResponse = ApiResponse.createResponseWithMessage(null, errorMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorMessage.getStatus().value());
        response.getWriter().write(json);
    }
}
