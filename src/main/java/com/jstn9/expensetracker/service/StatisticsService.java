package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.statistics.CategoryStats;
import com.jstn9.expensetracker.dto.statistics.DailyStats;
import com.jstn9.expensetracker.dto.statistics.IncomeExpense;
import com.jstn9.expensetracker.dto.statistics.MonthlyStats;
import com.jstn9.expensetracker.dto.transaction.TransactionResponse;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.repository.TransactionRepository;
import com.jstn9.expensetracker.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
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

        log.debug("Received income: {} , expense: {}", income, expense);
        log.info("Income-expense stats successfully fetched for userId: {}",user.getId());
        return new IncomeExpense(income,expense);
    }

    public List<CategoryStats> getCategoriesStats(LocalDate from, LocalDate to) {
        User user = userService.getCurrentUser();

        log.info("Categories stats successfully fetched for userId: {}",user.getId());
        return transactionRepository.getCategoriesStats(user, from, to);
    }

    public List<MonthlyStats> getMonthlyStats(int year) {
        User user = userService.getCurrentUser();

        log.info("Monthly stats successfully fetched for userId: {}",user.getId());
        return transactionRepository.getMonthlyStats(user, year);
    }

    public List<DailyStats> getDailyStats(int month, int year) {
        User user = userService.getCurrentUser();

        log.info("Daily stats successfully fetched for userId: {}",user.getId());
        return transactionRepository.getDailyStats(user, month, year);
    }

    public List<TransactionResponse> getLastTransactions(int count) {
        User user = userService.getCurrentUser();

        Pageable pageable = Pageable.ofSize(count);

        log.info("Last transactions successfully fetched for userId: {}",user.getId());
        return transactionRepository
                .findByUserOrderByDateDesc(user, pageable)
                .stream()
                .map(MapperUtil::toTransactionResponse)
                .toList();
    }
}
