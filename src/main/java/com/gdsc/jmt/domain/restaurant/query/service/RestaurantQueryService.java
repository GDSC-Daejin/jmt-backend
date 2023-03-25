package com.gdsc.jmt.domain.restaurant.query.service;

import com.gdsc.jmt.domain.restaurant.util.KaKaoSearchResponse;
import com.gdsc.jmt.domain.restaurant.util.RestaurantAPIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantQueryService {


    public void findRestaurantLocationList(final String query, final Integer page) {
         KaKaoSearchResponse response = RestaurantAPIUtil.findRestaurantLocation(query, page);

    }
}
