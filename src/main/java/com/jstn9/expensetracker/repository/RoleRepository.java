package com.jstn9.expensetracker.repository;

import com.jstn9.expensetracker.model.Role;
import com.jstn9.expensetracker.model.enums.RoleNames;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleNames name);
}
