package com.jstn9.expensetracker.mapper;

import com.jstn9.expensetracker.dto.category.CategoryResponse;
import com.jstn9.expensetracker.model.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);
}
