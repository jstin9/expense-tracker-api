package com.jstn9.expensetracker.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryUpdateRequest {

    @NotBlank(message = "Category name cannot be empty!")
    private String name;
}
