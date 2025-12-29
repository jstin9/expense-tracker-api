package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.transaction.TransactionRequest;
import com.jstn9.expensetracker.exception.BalanceNegativeException;
import com.jstn9.expensetracker.exception.CategoryNotFoundException;
import com.jstn9.expensetracker.model.Category;
import com.jstn9.expensetracker.model.Profile;
import com.jstn9.expensetracker.model.Transaction;
import com.jstn9.expensetracker.model.User;
import com.jstn9.expensetracker.model.enums.TransactionType;
import com.jstn9.expensetracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionBalanceService {

    private final CategoryRepository categoryRepository;

    public TransactionBalanceService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void rollbackOldTransactionEffect(Profile profile, Transaction oldTransaction) {
        BigDecimal amount = oldTransaction.getAmount();
        TransactionType type = oldTransaction.getType();
        BigDecimal oldBalance = profile.getBalance();

        if (type == TransactionType.INCOME) {
            profile.setBalance(oldBalance.subtract(amount));
        } else {
            profile.setBalance(oldBalance.add(amount));
        }

    }

    public void applyNewBalance(Profile profile, Transaction transaction) {
        BigDecimal amount = transaction.getAmount();
        TransactionType type = transaction.getType();
        BigDecimal oldBalance = profile.getBalance();

        if (type == TransactionType.INCOME) {
            profile.setBalance(oldBalance.add(amount));
        } else {
            profile.setBalance(oldBalance.subtract(amount));
        }

        if (profile.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceNegativeException();
        }
    }

    public void fillTransactionFromRequest(Transaction transaction,
                                           TransactionRequest request,
                                           User user) {
        Category category = getCategoryForUser(request, user);

        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());

    }

    private Category getCategoryForUser(TransactionRequest request, User user) {
        return categoryRepository
                .findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(CategoryNotFoundException::new);
    }
}
