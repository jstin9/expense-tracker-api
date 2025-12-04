package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.statistics.CategoryStats;
import com.jstn9.expensetracker.dto.statistics.IncomeExpense;
import com.jstn9.expensetracker.dto.statistics.MonthlyStats;
import com.jstn9.expensetracker.dto.transaction.TransactionResponse;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.repository.TransactionRepository;
import com.jstn9.expensetracker.util.MapperUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public List<TransactionResponse> getLastTransactions(int count) {
        User user = userService.getCurrentUser();

        Pageable pageable = Pageable.ofSize(count);

        return transactionRepository
                .findByUserOrderByDateDesc(user, pageable)
                .stream()
                .map(MapperUtil::toTransactionResponse)
                .toList();
    }
}
