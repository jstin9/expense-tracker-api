package com.jstn9.expensetracker.dto.statistics;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyStats {

    public MonthlyStats(int month, BigDecimal income, BigDecimal expense) {
        this.month = month;
        this.income = income;
        this.expense = expense;
    }

    private int month;
    private BigDecimal income;
    private BigDecimal expense;
}
