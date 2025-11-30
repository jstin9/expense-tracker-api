    package com.jstn9.expensetracker.repository;

    import com.jstn9.expensetracker.dto.statistics.TypeStats;
    import com.jstn9.expensetracker.models.Category;
    import com.jstn9.expensetracker.models.Transaction;
    import com.jstn9.expensetracker.models.User;
    import com.jstn9.expensetracker.models.enums.TransactionType;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.time.LocalDate;
    import java.util.List;
    import java.util.Optional;

    public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

        Optional<Transaction> findByIdAndUser(Long id, User user);

        boolean existsByCategory(Category category);

        @Query("""
            SELECT new com.jstn9.expensetracker.dto.statistics.TypeStats(
                t.type,
                SUM(t.amount)
            )
            FROM Transaction t
            WHERE t.user = :user
              AND t.date >= COALESCE(:fromDate, t.date)
              AND t.date <= COALESCE(:toDate, t.date)
              AND t.type = COALESCE(:transactionType, t.type)
            GROUP BY t.type
        """)
        List<TypeStats> getIncomeExpenseStats(
                @Param("user") User user,
                @Param("transactionType") TransactionType transactionType,
                @Param("fromDate") LocalDate fromDate,
                @Param("toDate") LocalDate toDate
        );
    }
