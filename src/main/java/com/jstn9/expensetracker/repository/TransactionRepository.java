    package com.jstn9.expensetracker.repository;

    import com.jstn9.expensetracker.dto.statistics.CategoryStats;
    import com.jstn9.expensetracker.dto.statistics.DailyStats;
    import com.jstn9.expensetracker.dto.statistics.MonthlyStats;
    import com.jstn9.expensetracker.model.Category;
    import com.jstn9.expensetracker.model.Transaction;
    import com.jstn9.expensetracker.model.User;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.util.List;
    import java.util.Optional;

    public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

        Optional<Transaction> findByIdAndUser(Long id, User user);

        boolean existsByCategory(Category category);

        @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user = :user
              AND t.type = 'INCOME'
              AND (CAST(:fromDate AS date) IS NULL OR t.date >= :fromDate)
              AND (CAST(:toDate  AS date) IS NULL OR t.date <= :toDate)
        """)
        BigDecimal getIncome(
                @Param("user") User user,
                @Param("fromDate") LocalDate fromDate,
                @Param("toDate") LocalDate toDate
        );

        @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user = :user
              AND t.type = 'EXPENSE'
              AND (CAST(:fromDate AS date) IS NULL OR t.date >= :fromDate)
              AND (CAST(:toDate  AS date) IS NULL OR t.date <= :toDate)
        """)
        BigDecimal getExpense(
                @Param("user") User user,
                @Param("fromDate") LocalDate fromDate,
                @Param("toDate") LocalDate toDate
        );

        @Query("""
            SELECT new com.jstn9.expensetracker.dto.statistics.CategoryStats(
                new com.jstn9.expensetracker.dto.category.CategoryResponse(c.id, c.name),
                SUM(t.amount)
            )
            FROM Transaction t
            JOIN t.category c
            WHERE t.user = :user
              AND t.date >= COALESCE(:fromDate, t.date)
              AND t.date <= COALESCE(:toDate, t.date)
            GROUP BY c.id, c.name
        """)
        List<CategoryStats> getCategoriesStats(
                @Param("user") User user,
                @Param("fromDate") LocalDate from,
                @Param("toDate") LocalDate to);


        @Query("""
            SELECT new com.jstn9.expensetracker.dto.statistics.MonthlyStats(
                MONTH(t.date),
                SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END),
                SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END)
            )
            FROM Transaction t
            WHERE t.user = :user
              AND YEAR(t.date) = :year
            GROUP BY MONTH(t.date)
            ORDER BY MONTH(t.date)
        """)
        List<MonthlyStats> getMonthlyStats(
                @Param("user") User user,
                @Param("year") int year);

        @Query("""
        SELECT new com.jstn9.expensetracker.dto.statistics.DailyStats(
            DAY(t.date),
            SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END),
            SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END)
        )
        FROM Transaction t
        WHERE t.user = :user
          AND MONTH(t.date) = :month
          AND YEAR(t.date) = :year
        GROUP BY DAY(t.date)
        ORDER BY DAY(t.date)
        """)
        List<DailyStats> getDailyStats(
                @Param("user") User user,
                @Param("month") int month,
                @Param("year") int year);

        List<Transaction> findByUserOrderByDateDesc(User user, Pageable pageable);
    }
