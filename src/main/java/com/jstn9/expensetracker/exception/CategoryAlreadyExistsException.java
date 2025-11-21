package com.jstn9.expensetracker.exception;

public class CategoryAlreadyExistsException extends FieldException {
    public CategoryAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public String getField() {
        return "name";
    }
}
