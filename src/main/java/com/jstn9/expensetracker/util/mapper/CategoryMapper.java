package com.jstn9.expensetracker.util.mapper;

import com.jstn9.expensetracker.dto.category.CategoryResponse;
import com.jstn9.expensetracker.models.Category;

public class CategoryMapper {

    public static CategoryResponse toCategoryResponse(Category category){
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        return categoryResponse;
    }
}
