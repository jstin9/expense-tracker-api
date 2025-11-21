package com.jstn9.expensetracker.exception;

public class CategoryNotFoundException extends FieldException {
    public CategoryNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getField() {
        return "category_id";
    }
}
