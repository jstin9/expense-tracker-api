package com.jstn9.expensetracker.dto.transaction;

import com.jstn9.expensetracker.models.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionResponse {

    private Long id;

    private BigDecimal amount;

    private TransactionType type;

    private String description;

    private LocalDate date;
}
