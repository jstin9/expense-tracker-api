package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.transaction.TransactionFilter;
import com.jstn9.expensetracker.dto.transaction.TransactionRequest;
import com.jstn9.expensetracker.dto.transaction.TransactionResponse;
import com.jstn9.expensetracker.exception.BalanceException;
import com.jstn9.expensetracker.exception.CategoryNotFoundException;
import com.jstn9.expensetracker.exception.TransactionNotFoundException;
import com.jstn9.expensetracker.models.Category;
import com.jstn9.expensetracker.models.Profile;
import com.jstn9.expensetracker.models.Transaction;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.models.enums.TransactionType;
import com.jstn9.expensetracker.repository.CategoryRepository;
import com.jstn9.expensetracker.repository.ProfileRepository;
import com.jstn9.expensetracker.repository.TransactionRepository;
import com.jstn9.expensetracker.specification.TransactionSpecification;
import com.jstn9.expensetracker.util.MapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileRepository profileRepository;
    private final UserService userService;

    public TransactionService(TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository,
                              ProfileRepository profileRepository,
                              UserService userService) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.profileRepository = profileRepository;
        this.userService = userService;
    }

    public Page<TransactionResponse> getFiltered(TransactionFilter filter, Pageable pageable){
        User user = userService.getCurrentUser();

        return transactionRepository.findAll(
                TransactionSpecification.filter(filter, user),
                pageable).map(MapperUtil::toTransactionResponse);

    }

    public TransactionResponse getTransactionById(Long id) {
        User user = userService.getCurrentUser();
        return transactionRepository.findByIdAndUser(id, user)
                .map(MapperUtil::toTransactionResponse)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found by id: " + id));
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        Profile profile = getCurrentUserProfile();

        Transaction transaction = new Transaction();

        fillTransactionFromRequest(transaction,request);
        applyNewBalance(profile,transaction);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return MapperUtil.toTransactionResponse(savedTransaction);
    }

    @Transactional
    public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
        User user = userService.getCurrentUser();

        Transaction oldTransaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found by id: " + id));

        Profile profile = getCurrentUserProfile();

        boolean amountChanged = oldTransaction.getAmount().compareTo(request.getAmount()) != 0;
        boolean typeChanged = oldTransaction.getType() != request.getType();

        //update balance only if amount or type changed
        if(amountChanged || typeChanged) {
            rollbackOldTransactionEffect(profile, oldTransaction);
        }

        //update transaction
        fillTransactionFromRequest(oldTransaction,request);

        //save balance only if amount or type changed
        if(amountChanged || typeChanged) {
            applyNewBalance(profile, oldTransaction);
            profileRepository.save(profile);
        }

        Transaction savedTransaction = transactionRepository.save(oldTransaction);
        return MapperUtil.toTransactionResponse(savedTransaction);
    }

    @Transactional
    public void deleteById(Long id) {
        User user = userService.getCurrentUser();

        Transaction oldTransaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found by id:" + id));

        Profile profile = getCurrentUserProfile();

        rollbackOldTransactionEffect(profile, oldTransaction);

        if(profile.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceException("Balance cannot be negative after deletion!");
        }

        profileRepository.save(profile);

        transactionRepository.delete(oldTransaction);
    }

    private void fillTransactionFromRequest(Transaction transaction,
                                            TransactionRequest request) {
        User user = userService.getCurrentUser();

        Category category = categoryRepository
                .findByIdAndUser(request.getCategory_id(), user)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
    }

    private void rollbackOldTransactionEffect(Profile profile, Transaction oldTransaction) {
        BigDecimal amount = oldTransaction.getAmount();

        if(oldTransaction.getType() == TransactionType.INCOME){
            profile.setBalance(profile.getBalance().subtract(amount));
        } else {
            profile.setBalance(profile.getBalance().add(amount));
        }
    }

    private void applyNewBalance(Profile profile, Transaction transaction) {
        BigDecimal amount = transaction.getAmount();

        if(transaction.getType() == TransactionType.INCOME){
            profile.setBalance(profile.getBalance().add(amount));
        } else {
            profile.setBalance(profile.getBalance().subtract(amount));
        }

        if(profile.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceException("Balance can't be negative!");
        }
    }

    private Profile getCurrentUserProfile() {
        User user = userService.getCurrentUser();
        return profileRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
