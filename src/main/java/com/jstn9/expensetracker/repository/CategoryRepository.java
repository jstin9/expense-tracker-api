package com.jstn9.expensetracker.repository;

import com.jstn9.expensetracker.models.Category;
import com.jstn9.expensetracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByUser(User user);

    Optional<Category> findByIdAndUser(Long id, User user);

    boolean existsByNameAndUser(String name, User user);

}
