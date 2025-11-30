package com.jstn9.expensetracker.exception;

public class CategoryIsUsedInTransactionException extends RuntimeException {
    public CategoryIsUsedInTransactionException(String message) {
        super(message);
    }
}
