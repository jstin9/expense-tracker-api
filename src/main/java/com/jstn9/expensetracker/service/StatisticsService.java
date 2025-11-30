package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.statistics.TypeStats;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.models.enums.TransactionType;
import com.jstn9.expensetracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public StatisticsService(TransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    public List<TypeStats> getIncomeExpenseStats(TransactionType type, LocalDate from, LocalDate to) {
        User user = userService.getCurrentUser();
        return transactionRepository.getIncomeExpenseStats(user, type, from, to);
    }
}
