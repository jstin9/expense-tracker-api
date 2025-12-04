package com.jstn9.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class UsernameNotFoundException extends ApiException {
    public UsernameNotFoundException(){
        super(ErrorCode.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND, "username");
    }
}
