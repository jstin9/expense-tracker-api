package com.jstn9.expensetracker.dto.statistics;

import com.jstn9.expensetracker.models.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TypeStats {

    public TypeStats(TransactionType transactionType, BigDecimal amount) {
        this.transactionType = transactionType;
        this.amount = amount;
    }

    private TransactionType transactionType;
    private BigDecimal amount;
}
