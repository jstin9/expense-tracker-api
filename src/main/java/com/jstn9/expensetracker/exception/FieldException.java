package com.jstn9.expensetracker.exception;

import lombok.Getter;

@Getter
public abstract class FieldException extends RuntimeException {
    public abstract String getField();

    public FieldException(String message) {
        super(message);
    }

}
