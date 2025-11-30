package com.jstn9.expensetracker.controller;

import com.jstn9.expensetracker.dto.statistics.TypeStats;
import com.jstn9.expensetracker.models.enums.TransactionType;
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
    public List<TypeStats> getIncomeExpenseStats(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ){
        return statisticsService.getIncomeExpenseStats(type, from, to);
    }

}
