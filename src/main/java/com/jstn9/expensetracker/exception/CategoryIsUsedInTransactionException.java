package com.jstn9.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class CategoryIsUsedInTransactionException extends ApiException {
    public CategoryIsUsedInTransactionException() {
        super(ErrorCode.CATEGORY_IN_USE, HttpStatus.BAD_REQUEST, "category");
    }
}
