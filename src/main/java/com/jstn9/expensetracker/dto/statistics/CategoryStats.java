package com.jstn9.expensetracker.dto.statistics;

import com.jstn9.expensetracker.dto.category.CategoryResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CategoryStats {

    public CategoryStats(CategoryResponse categoryResponse, BigDecimal amount) {
        this.categoryResponse = categoryResponse;
        this.amount = amount;
    }

    private CategoryResponse categoryResponse;
    private BigDecimal amount;
}
