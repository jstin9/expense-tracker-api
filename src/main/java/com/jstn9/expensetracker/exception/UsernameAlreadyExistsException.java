package com.jstn9.expensetracker.exception;

public class UsernameAlreadyExistsException extends FieldException {
    public UsernameAlreadyExistsException(String username) {
        super("username", "User with username " + username + " already exists");
    }
}
