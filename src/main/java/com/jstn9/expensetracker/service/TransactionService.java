package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.transaction.TransactionFilter;
import com.jstn9.expensetracker.dto.transaction.TransactionRequest;
import com.jstn9.expensetracker.dto.transaction.TransactionResponse;
import com.jstn9.expensetracker.exception.BalanceNegativeException;
import com.jstn9.expensetracker.exception.CategoryNotFoundException;
import com.jstn9.expensetracker.exception.TransactionNotFoundException;
import com.jstn9.expensetracker.exception.UsernameNotFoundException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
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

        log.info("Fetching filtered transactions for userId: {}, username: {}",
                user.getId(), user.getUsername());
        log.debug("Filter parameters: {}", filter);

        return transactionRepository.findAll(TransactionSpecification
                .filter(filter, user), pageable)
                .map(MapperUtil::toTransactionResponse);
    }

    public TransactionResponse getTransactionById(Long id) {
        User user = userService.getCurrentUser();
        log.info("Start fetching transaction with id: {} for user: {}", id, user.getUsername());

        Transaction transaction = transactionRepository.findByIdAndUser(id, user).orElseThrow(() -> {
            log.warn("Transaction not found with id: {}, user: {}", id, user.getUsername());
            return new TransactionNotFoundException();
        });

        log.info("Transaction fetched successfully with id: {} for user: {}", id, user.getUsername());
        return MapperUtil.toTransactionResponse(transaction);
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        Profile profile = getCurrentUserProfile();
        log.info("Starting create transaction for profileId: {}", profile.getId());

        Transaction transaction = new Transaction();

        fillTransactionFromRequest(transaction,request);
        applyNewBalance(profile,transaction);

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully with id: {}, profileId: {}",
                savedTransaction.getId(), profile.getId());

        return MapperUtil.toTransactionResponse(savedTransaction);
    }

    @Transactional
    public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
        User user = userService.getCurrentUser();
        Profile profile = getCurrentUserProfile();

        log.info("Starting update transaction for transactionId: {}, profileId: {}", id, profile.getId());

        Transaction oldTransaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> {
                    log.warn("Transaction not found for transactionId: {}, profileId: {}",
                            id, profile.getId());
                    return new TransactionNotFoundException();
                });


        boolean amountChanged = oldTransaction.getAmount().compareTo(request.getAmount()) != 0;
        boolean typeChanged = oldTransaction.getType() != request.getType();

        log.debug("Transaction amountChanged={}, typeChanged={} for id={}",
                amountChanged, typeChanged, id);

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
        log.info("Transaction updated successfully with id: {}, profileId: {}",
                savedTransaction.getId(), profile.getId());
        return MapperUtil.toTransactionResponse(savedTransaction);
    }

    @Transactional
    public void deleteById(Long id) {
        User user = userService.getCurrentUser();
        Profile profile = getCurrentUserProfile();

        log.info("Starting delete transaction for transactionId: {}, profileId: {}",
                id, profile.getId());

        Transaction oldTransaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> {
                    log.warn("Transaction not found for transactionId: {}, profileId: {}",
                            id, profile.getId());
                    return new TransactionNotFoundException();
                });

        rollbackOldTransactionEffect(profile, oldTransaction);

        if(profile.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Balance cannot be negative after deleting transactionId: {}, profileId: {}",
                    id, profile.getId());
            throw new BalanceNegativeException();
        }

        profileRepository.save(profile);

        transactionRepository.delete(oldTransaction);
        log.info("Transaction deleted successfully with id: {}, profileId: {}",
                id, profile.getId());
    }

    private void fillTransactionFromRequest(Transaction transaction,
                                            TransactionRequest request) {
        User user = userService.getCurrentUser();
        Profile profile = getCurrentUserProfile();
        log.debug("Start filling transaction (new or existing) for profileId: {}", profile.getId());

        Category category = categoryRepository
                .findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> {
                    log.warn("Category not found with id: {}", request.getCategoryId());
                    return new CategoryNotFoundException();
                });

        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());

        log.debug("Transaction filled successfully with id: {}, profileId: {}",
                transaction.getId(), profile.getId());
    }

    private void rollbackOldTransactionEffect(Profile profile, Transaction oldTransaction) {
        BigDecimal amount = oldTransaction.getAmount();
        TransactionType type = oldTransaction.getType();
        BigDecimal oldBalance = profile.getBalance();

        log.debug(
                "Rollback start: profileId={}, transactionId={}, type={}, amount={}, balanceBefore={}",
                profile.getId(), oldTransaction.getId(), type, amount, oldBalance
        );

        if (type == TransactionType.INCOME) {
            profile.setBalance(oldBalance.subtract(amount));
        } else {
            profile.setBalance(oldBalance.add(amount));
        }

        log.debug(
                "Rollback complete: profileId={}, transactionId={}, type={}, amount={}, balanceAfter={}",
                profile.getId(), oldTransaction.getId(), type, amount, profile.getBalance()
        );
    }

    private void applyNewBalance(Profile profile, Transaction transaction) {
        BigDecimal amount = transaction.getAmount();
        TransactionType type = transaction.getType();
        BigDecimal oldBalance = profile.getBalance();

        log.debug(
                "Apply balance start: profileId={}, transactionId={}, type={}, amount={}, balanceBefore={}",
                profile.getId(), transaction.getId(), type, amount, oldBalance
        );

        if (type == TransactionType.INCOME) {
            profile.setBalance(oldBalance.add(amount));
        } else {
            profile.setBalance(oldBalance.subtract(amount));
        }

        if (profile.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            log.warn(
                    "Balance negative after apply: profileId={}, transactionId={}, type={}, amount={}, balanceAfter={}",
                    profile.getId(), transaction.getId(), type, amount, profile.getBalance()
            );
            throw new BalanceNegativeException();
        }

        log.debug(
                "Apply balance complete: profileId={}, transactionId={}, type={}, amount={}, balanceAfter={}",
                profile.getId(), transaction.getId(), type, amount, profile.getBalance()
        );
    }

    private Profile getCurrentUserProfile() {
        log.debug("Getting current user profile");
        User user = userService.getCurrentUser();
        return profileRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.warn("Could not find current user profile for user: {}", user.getUsername());
                    return new UsernameNotFoundException();
                });
    }
}
