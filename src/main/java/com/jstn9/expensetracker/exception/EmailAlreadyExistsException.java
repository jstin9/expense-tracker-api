package com.jstn9.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ApiException {
    public EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT, "email");
    }
}
