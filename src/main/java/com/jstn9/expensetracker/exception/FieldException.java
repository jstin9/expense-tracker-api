package com.jstn9.expensetracker.exception;

import lombok.Getter;

@Getter
public abstract class FieldException extends RuntimeException {
    private final String field;

    public FieldException(String field, String message) {
        super(message);
        this.field = field;
    }

}
