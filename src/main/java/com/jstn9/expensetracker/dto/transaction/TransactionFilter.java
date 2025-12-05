package com.jstn9.expensetracker.dto.transaction;

import com.jstn9.expensetracker.models.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionFilter {
    private TransactionType type;
    private Long categoryId;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private LocalDate startDate;
    private LocalDate endDate;
}
