package com.jstn9.expensetracker.dto.statistics;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DailyStats {

    public DailyStats(int day, BigDecimal income, BigDecimal expense) {
        this.day = day;
        this.income = income;
        this.expense = expense;
    }

    private int day;
    private BigDecimal income;
    private BigDecimal expense;
}
