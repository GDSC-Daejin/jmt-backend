package com.gdsc.jmt.domain.category.query.service;

import com.gdsc.jmt.domain.category.query.dto.CategoriesResponse;
import com.gdsc.jmt.domain.category.query.dto.CategoryResponse;
import com.gdsc.jmt.domain.category.query.entity.CategoryEntity;
import com.gdsc.jmt.domain.category.query.repository.CategoryRepository;
import com.gdsc.jmt.global.exception.ApiException;
import com.gdsc.jmt.global.messege.CategoryMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Import({CategoryService.class})
public class CategoryServiceTest {

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @Nested
    @DisplayName("CategoryService findALl Test")
    class findAllTest {
        @Test
        public void 카테고리_정보가_있을_때() {
            // given
            List<CategoryEntity> categories = makeCategories();
            Mockito.when(categoryRepository.findAll())
                    .thenReturn(categories);

            // when
            CategoriesResponse result = categoryService.findAll();

            //then
            CategoriesResponse prediction = new CategoriesResponse(categories.stream().map(CategoryEntity::toResponse).toList());
            Assertions.assertArrayEquals(result.categories().toArray(), prediction.categories().toArray());
        }

        @Test
        public void 카테고리_정보가_없을_때() {
            // given
            // when
            CategoriesResponse result = categoryService.findAll();

            //then
            Assertions.assertTrue(result.categories().isEmpty());
        }

    }

    @Nested
    @DisplayName("CategoryService findCategory Test")
    class findCategoryTest {

        @Test
        public void 해당_카테고리가_있을_때() {
            // given
            Long categoryId = 1L;
            Optional<CategoryEntity> categoryOptional = Optional.of(makeCategories().get(0));
            Mockito.when(categoryRepository.findById(categoryId))
                    .thenReturn(categoryOptional);
            // when
            CategoryResponse result = categoryService.findCategory(categoryId);

            //then
            Assertions.assertEquals(result, categoryOptional.get().toResponse());
        }

        @Test
        public void 해당_카테고리가_없을_때() {
            // given
            Long categoryId = 1L;
            // when then
            ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
                categoryService.findCategory(categoryId);
            });
            Assertions.assertEquals(exception.getResponseMessage(), CategoryMessage.CATEGORY_FIND_FAIL);
        }

    }


    private List<CategoryEntity> makeCategories() {
        CategoryEntity category = new CategoryEntity();
        category.initForTest(1L, "한식");
        CategoryEntity category2 = new CategoryEntity();
        category2.initForTest(2L, "일식");

        List<CategoryEntity> categories = new ArrayList<>();
        categories.add(category);
        categories.add(category2);
        return categories;
    }

}
