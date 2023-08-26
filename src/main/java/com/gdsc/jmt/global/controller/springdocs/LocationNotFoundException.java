package com.gdsc.jmt.global.controller.springdocs;

import com.gdsc.jmt.global.messege.LocationMessage;
import io.swagger.v3.oas.annotations.media.Schema;

public class LocationNotFoundException {
    @Schema(description = "", nullable = true)
    String data = null;

    @Schema(description = "", example = "user 정보를 찾을 수 없습니다.")
    String message = LocationMessage.LOCATION_NOT_FOUND.getMessage();

    @Schema(description = "", example = "USER_NOT_FOUND")
    String code = LocationMessage.LOCATION_NOT_FOUND.toString();
}
