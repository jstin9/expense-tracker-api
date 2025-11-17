package com.jstn9.expensetracker.util.mapper;

import com.jstn9.expensetracker.dto.transaction.TransactionResponse;
import com.jstn9.expensetracker.models.Transaction;

public class TransactionMapper {
    public static TransactionResponse toTransactionResponse(Transaction transaction) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setId(transaction.getId());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setType(transaction.getType());
        transactionResponse.setDescription(transaction.getDescription());
        transactionResponse.setDate(transaction.getDate());
        return transactionResponse;
    }
}
