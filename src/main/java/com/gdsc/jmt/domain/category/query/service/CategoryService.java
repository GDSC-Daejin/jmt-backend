package com.gdsc.jmt.domain.category.query.service;

import com.gdsc.jmt.domain.category.query.dto.CategoriesResponse;
import com.gdsc.jmt.domain.category.query.dto.CategoryResponse;
import com.gdsc.jmt.domain.category.query.entity.CategoryEntity;
import com.gdsc.jmt.domain.category.query.repository.CategoryRepository;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.CategoryMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoriesResponse findAll() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryHolder = categories.stream().map(CategoryEntity::toResponse).toList();
        return new CategoriesResponse(categoryHolder);
    }

    public CategoryResponse findCategory(Long id) {
        Optional<CategoryEntity> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            throw new ApiException(CategoryMessage.CATEGORY_FIND_FAIL);
        }
        return category.get().toResponse();
    }
}
