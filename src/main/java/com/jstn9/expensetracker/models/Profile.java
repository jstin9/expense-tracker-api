package com.jstn9.expensetracker.models;

import com.jstn9.expensetracker.models.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "profile")
public class Profile {
    @Id
    private Long id;

    private String name;

    private BigDecimal balance;

    private BigDecimal monthSalary;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
