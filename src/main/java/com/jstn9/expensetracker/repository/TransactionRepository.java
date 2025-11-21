package com.jstn9.expensetracker.repository;

import com.jstn9.expensetracker.models.Transaction;
import com.jstn9.expensetracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    List<Transaction> getAllByUser(User user);

    Optional<Transaction> findByUserAndId(User user, Long id);

    Optional<Transaction> findByIdAndUser(Long id, User user);
}
