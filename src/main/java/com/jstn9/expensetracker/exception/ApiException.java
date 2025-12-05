package com.jstn9.expensetracker.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String field;

    ApiException(ErrorCode errorCode, HttpStatus httpStatus) {
        this(errorCode, httpStatus, null);
    }

    ApiException(ErrorCode errorCode, HttpStatus httpStatus, String field) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.field = field;
    }

}
