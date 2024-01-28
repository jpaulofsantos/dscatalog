package com.developer.dscatalog.dto;

import com.developer.dscatalog.entities.Category;

import java.time.Instant;

public class CategoryDTO {

    private Long id;
    private String name;

    private Instant created;

    public CategoryDTO() {

    }

    public CategoryDTO(Long id, String name, Instant created) {
        this.id = id;
        this.name = name;
        this.created = created;
    }

    public CategoryDTO(Category category) {
        id = category.getId();
        name = category.getName();
        created = category.getCreatedAt();

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreated() {
        return created;
    }
}
