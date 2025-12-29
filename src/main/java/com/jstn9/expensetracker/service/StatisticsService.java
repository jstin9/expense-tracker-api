package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.statistics.CategoryStats;
import com.jstn9.expensetracker.dto.statistics.DailyStats;
import com.jstn9.expensetracker.dto.statistics.IncomeExpense;
import com.jstn9.expensetracker.dto.statistics.MonthlyStats;
import com.jstn9.expensetracker.dto.transaction.TransactionResponse;
import com.jstn9.expensetracker.mapper.TransactionMapper;
import com.jstn9.expensetracker.model.User;
import com.jstn9.expensetracker.repository.TransactionRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TransactionMapper transactionMapper;

    public StatisticsService(TransactionRepository transactionRepository, UserService userService, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.transactionMapper = transactionMapper;
    }

    public IncomeExpense getIncomeExpenseStats(LocalDate from, LocalDate to) {
        User user = userService.getCurrentUser();

        BigDecimal income = transactionRepository.getIncome(user, from, to);
        BigDecimal expense = transactionRepository.getExpense(user, from, to);

        return new IncomeExpense(income,expense);
    }

    public List<CategoryStats> getCategoriesStats(LocalDate from, LocalDate to) {
        User user = userService.getCurrentUser();

        return transactionRepository.getCategoriesStats(user, from, to);
    }

    public List<MonthlyStats> getMonthlyStats(int year) {
        User user = userService.getCurrentUser();

        return transactionRepository.getMonthlyStats(user, year);
    }

    public List<DailyStats> getDailyStats(int month, int year) {
        User user = userService.getCurrentUser();

        return transactionRepository.getDailyStats(user, month, year);
    }

    public List<TransactionResponse> getLastTransactions(int count) {
        User user = userService.getCurrentUser();

        Pageable pageable = Pageable.ofSize(count);

        return transactionRepository
                .findByUserOrderByDateDesc(user, pageable)
                .stream()
                .map(transactionMapper::toTransactionResponse)
                .toList();
    }
}
