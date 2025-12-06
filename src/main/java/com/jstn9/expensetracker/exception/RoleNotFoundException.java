package com.jstn9.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends ApiException {
    public RoleNotFoundException() {
        super(ErrorCode.ROLE_NOT_FOUND, HttpStatus.NOT_FOUND, "role");
    }
}
