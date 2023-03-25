package com.gdsc.jmt.domain.restaurant.query.service;

import com.gdsc.jmt.domain.restaurant.naver.NaverUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantQueryService {


    public void findRestaurantLocationList(final String query) {
         = NaverUtil.findRestaurantLocation(query);
    }
}
