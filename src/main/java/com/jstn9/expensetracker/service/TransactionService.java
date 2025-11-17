package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.transaction.TransactionRequest;
import com.jstn9.expensetracker.dto.transaction.TransactionResponse;
import com.jstn9.expensetracker.models.Category;
import com.jstn9.expensetracker.models.Transaction;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.repository.CategoryRepository;
import com.jstn9.expensetracker.repository.TransactionRepository;
import com.jstn9.expensetracker.util.mapper.TransactionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public List<TransactionResponse> getTransactions() {
        User user = userService.getCurrentUser();
        return transactionRepository.getAllByUser(user)
                .stream()
                .map(TransactionMapper::toTransactionResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse getTransactionById(Long id) {
        User user = userService.getCurrentUser();
        return transactionRepository.findByUserAndId(user, id)
                .map(TransactionMapper::toTransactionResponse)
                .orElseThrow(() -> new RuntimeException("Transaction not found by id: " + id));
    }

    public TransactionResponse createTransaction(TransactionRequest request) {
        User user = userService.getCurrentUser();
        Transaction transaction = new Transaction();

        fillTransactionFromRequest(transaction,request,user);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionMapper.toTransactionResponse(savedTransaction);
    }

    public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
        User user = userService.getCurrentUser();
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found by id: " + id));

        fillTransactionFromRequest(transaction,request,user);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionMapper.toTransactionResponse(savedTransaction);
    }

    public void deleteById(Long id) {
        User user = userService.getCurrentUser();

        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Transaction not found by id:" + id));

        transactionRepository.delete(transaction);
    }

    private void fillTransactionFromRequest(Transaction transaction, TransactionRequest request, User user) {
        Category category = categoryRepository
                .findByIdAndUser(request.getCategory_id(), user)
                .orElseThrow(() -> new RuntimeException
                        ("Category not found by id: " + request.getCategory_id()));

        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
    }
}
