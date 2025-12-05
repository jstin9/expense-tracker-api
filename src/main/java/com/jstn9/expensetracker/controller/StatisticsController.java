package com.jstn9.expensetracker.controller;

import com.jstn9.expensetracker.dto.statistics.CategoryStats;
import com.jstn9.expensetracker.dto.statistics.DailyStats;
import com.jstn9.expensetracker.dto.statistics.IncomeExpense;
import com.jstn9.expensetracker.dto.statistics.MonthlyStats;
import com.jstn9.expensetracker.dto.transaction.TransactionResponse;
import com.jstn9.expensetracker.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/income-expense")
    public IncomeExpense getIncomeExpenseStats(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ){
        return statisticsService.getIncomeExpenseStats(from, to);
    }

    @GetMapping("/categories")
    public List<CategoryStats> getCategoriesStats(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ){
        return statisticsService.getCategoriesStats(from,to);
    }

    @GetMapping("/monthly")
    public List<MonthlyStats> getMonthlyStats(
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now().getYear()}") int year
    ){
        return statisticsService.getMonthlyStats(year);
    }

    @GetMapping("/daily")
    public List<DailyStats> getDailyStats(
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") int month,
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now().getYear()}") int year
    ){
        return statisticsService.getDailyStats(month, year);
    }

    @GetMapping("/last-transactions")
    public List<TransactionResponse> getLastTransactions(
            @RequestParam(required = false, defaultValue = "5") int limit
    ){
        return statisticsService.getLastTransactions(limit);
    }
}
