package com.jstn9.expensetracker.exception;

public class EmailAlreadyExistsException extends FieldException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public String getField() {
        return "email";
    }
}
