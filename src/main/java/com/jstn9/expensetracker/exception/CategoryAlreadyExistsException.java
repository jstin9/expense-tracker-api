package com.jstn9.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class CategoryAlreadyExistsException extends ApiException {
    public CategoryAlreadyExistsException() {
        super(ErrorCode.CATEGORY_ALREADY_EXISTS, HttpStatus.CONFLICT, "name");
    }
}
