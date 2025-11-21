package com.jstn9.expensetracker.exception;

public class UsernameAlreadyExistsException extends FieldException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public String getField() {
        return "username";
    }
}
