package com.gdsc.jmt.domain.category.query.entity;

import com.gdsc.jmt.domain.category.query.dto.CategoryResponse;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity @Table(name = "tb_category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String codeName;

    public void initForTest(Long id, String name, String codeName) {
        this.id = id;
        this.name = name;
        this.codeName = codeName;
    }

    public CategoryResponse toResponse() {
        return new CategoryResponse(id, name, codeName);
    }
}
