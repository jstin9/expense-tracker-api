package com.jstn9.expensetracker.dto.category;

import lombok.Data;

@Data
public class CategoryResponse {

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;
    private String name;
}
