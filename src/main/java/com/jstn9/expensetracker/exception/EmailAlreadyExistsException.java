package com.jstn9.expensetracker.exception;

public class EmailAlreadyExistsException extends FieldException {
    public EmailAlreadyExistsException(String email) {
        super("email", "User with email " + email + " already exists");
    }
}
