package com.gdsc.jmt.domain.category.query.controller;

import com.gdsc.jmt.domain.category.query.controller.springdocs.CategoriesSpringDocs;
import com.gdsc.jmt.domain.category.query.controller.springdocs.CategorySpringDocs;
import com.gdsc.jmt.domain.category.query.dto.CategoriesResponse;
import com.gdsc.jmt.domain.category.query.dto.CategoryResponse;
import com.gdsc.jmt.domain.category.query.service.CategoryService;
import com.gdsc.jmt.global.controller.FirstVersionRestController;
import com.gdsc.jmt.global.dto.JMTApiResponse;
import com.gdsc.jmt.global.messege.CategoryMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FirstVersionRestController
@RequiredArgsConstructor
@Tag(name = "카테고리 컨트롤러")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    @CategoriesSpringDocs
    public JMTApiResponse<CategoriesResponse> getCategories() {
        CategoriesResponse categories = categoryService.findAll();
        return JMTApiResponse.createResponseWithMessage(categories, CategoryMessage.CATEGORY_LIST_SUCCESS);
    }

    @GetMapping("/category/{id}")
    @CategorySpringDocs
    public JMTApiResponse<CategoryResponse> getCategory(@PathVariable Long id) {
        CategoryResponse category = categoryService.findCategory(id);
        return JMTApiResponse.createResponseWithMessage(category, CategoryMessage.CATEGORY_FIND_SUCCESS);
    }
}
