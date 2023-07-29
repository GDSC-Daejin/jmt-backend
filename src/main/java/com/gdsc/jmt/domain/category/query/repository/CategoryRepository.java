package com.gdsc.jmt.domain.category.query.repository;


import com.gdsc.jmt.domain.category.query.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT category FROM CategoryEntity category WHERE category.codeName IN :codeNames")
    public List<CategoryEntity> findByCodeNames(List<String> codeNames);
}
