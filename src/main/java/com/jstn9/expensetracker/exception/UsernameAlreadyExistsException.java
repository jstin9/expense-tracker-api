package com.jstn9.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends ApiException {
    public UsernameAlreadyExistsException() {
        super(ErrorCode.USERNAME_ALREADY_EXISTS, HttpStatus.CONFLICT, "username");
    }

}
