package com.gdsc.jmt.domain.restaurant.query.service;

import com.gdsc.jmt.domain.category.query.entity.CategoryEntity;
import com.gdsc.jmt.domain.category.query.repository.CategoryRepository;
import com.gdsc.jmt.domain.restaurant.query.dto.request.RestaurantFilter;
import com.gdsc.jmt.domain.restaurant.query.dto.response.FindRestaurantItems;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantFilterService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<FindRestaurantItems> applyFilter(RestaurantFilter restaurantFilter, List<FindRestaurantItems> restaurantItems) {
        List<FindRestaurantItems> appliedFilterRestaurantItems = new ArrayList<>(restaurantItems);
        appliedFilterRestaurantItems = applyIsCanDrinkLiquorFilter(restaurantFilter, appliedFilterRestaurantItems);
        appliedFilterRestaurantItems = applyCategoryFilter(restaurantFilter, appliedFilterRestaurantItems);
        return appliedFilterRestaurantItems;
    }

    private List<FindRestaurantItems> applyIsCanDrinkLiquorFilter(RestaurantFilter restaurantFilter, List<FindRestaurantItems> restaurantItems) {
        if(restaurantFilter.isCanDrinkLiquor() == null) {
            return restaurantItems;
        }
        return restaurantItems.stream().filter(item -> item.canDrinkLiquor() == restaurantFilter.isCanDrinkLiquor()).toList();
    }

    private List<FindRestaurantItems> applyCategoryFilter(RestaurantFilter restaurantFilter, List<FindRestaurantItems> restaurantItems) {
        if(restaurantFilter.categoryFilter() == null || restaurantFilter.categoryFilter().isEmpty()) {
            return restaurantItems;
        }

        String[] categoryCodeNames = restaurantFilter.categoryFilter().split(",");
        List<CategoryEntity> categories = categoryRepository.findByCodeNames(Arrays.stream(restaurantFilter.categoryFilter().split(",")).toList());
        List<String> categoryNames = categories.stream().map(CategoryEntity::getName).toList();
        return restaurantItems.stream().filter(restaurant -> categoryNames.contains(restaurant.category())).toList();
    }

    public List<Long> findCategoryIdsByFilter(RestaurantFilter restaurantFilter) {
        if(restaurantFilter.categoryFilter() == null || restaurantFilter.categoryFilter().isEmpty()) {
            return categoryRepository.findAll().stream().map(CategoryEntity::getId).toList();
        }

        List<CategoryEntity> categories = categoryRepository.findByCodeNames(Arrays.stream(restaurantFilter.categoryFilter().split(",")).toList());
        List<Long> categoryIds = categories.stream().map(CategoryEntity::getId).toList();
        return categoryIds;
    }
}
