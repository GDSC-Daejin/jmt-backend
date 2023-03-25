package com.gdsc.jmt.domain.restaurant.naver;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class NaverLocationResponse {
    private LocalDateTime lastBuildDate;
    private Integer total;

    private Integer start;
    private Integer display;

    private List<NaverLocationItem> items;
}
