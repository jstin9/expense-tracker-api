package com.jstn9.expensetracker.repository;

import com.jstn9.expensetracker.models.Profile;
import com.jstn9.expensetracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
}
