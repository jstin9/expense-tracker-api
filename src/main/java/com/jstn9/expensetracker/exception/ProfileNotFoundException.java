package com.jstn9.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class ProfileNotFoundException extends ApiException {
    public ProfileNotFoundException() {
        super(ErrorCode.PROFILE_NOT_FOUND, HttpStatus.NOT_FOUND, "profile");
    }
}
