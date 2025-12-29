package com.jstn9.expensetracker.mapper;

import com.jstn9.expensetracker.dto.transaction.TransactionResponse;
import com.jstn9.expensetracker.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(uses =  CategoryMapper.class)
public interface TransactionMapper {
    TransactionResponse toTransactionResponse(Transaction transaction);
}
