package com.jstn9.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends ApiException {
    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND, "category");
    }
}
