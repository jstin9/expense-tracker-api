package com.jstn9.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class BalanceNegativeException extends ApiException {
    public BalanceNegativeException() {
        super(ErrorCode.BALANCE_IS_NEGATIVE, HttpStatus.CONFLICT, "balance");
    }
}
