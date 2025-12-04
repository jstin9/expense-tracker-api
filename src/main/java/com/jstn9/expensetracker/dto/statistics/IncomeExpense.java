package com.jstn9.expensetracker.dto.statistics;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class IncomeExpense {
    private BigDecimal income;
    private BigDecimal expense;

    public IncomeExpense(BigDecimal income, BigDecimal expense) {
        this.income = income;
        this.expense = expense;
    }
}
