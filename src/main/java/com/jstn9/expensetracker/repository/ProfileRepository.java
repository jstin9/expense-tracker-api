package com.jstn9.expensetracker.repository;

import com.jstn9.expensetracker.models.Profile;
import com.jstn9.expensetracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);

    @Query("""
        SELECT CASE WHEN p.createdAt = p.updatedAt THEN TRUE ELSE FALSE END
        FROM Profile p
        WHERE p.user.id = :userId
    """)
    boolean isProfileNeverUpdated(Long userId);
}
