package com.gdsc.jmt.domain.category.query.repository;


import com.gdsc.jmt.domain.category.query.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
