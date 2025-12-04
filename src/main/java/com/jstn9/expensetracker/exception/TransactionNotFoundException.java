package com.jstn9.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends ApiException {
  public TransactionNotFoundException() {
      super(ErrorCode.TRANSACTION_NOT_FOUND, HttpStatus.NOT_FOUND, "transaction");
  }
}
