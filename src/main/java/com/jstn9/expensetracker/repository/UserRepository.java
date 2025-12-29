package com.jstn9.expensetracker.repository;

import com.jstn9.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    Optional<User> findByUsername(String username);
}
