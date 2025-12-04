package com.jstn9.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class ProfileNotFilledException extends ApiException {
    public ProfileNotFilledException() {
        super(ErrorCode.PROFILE_NOT_FILLED, HttpStatus.BAD_REQUEST, "profile");
    }
}
