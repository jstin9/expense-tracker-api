package com.jstn9.expensetracker.model;

import com.jstn9.expensetracker.model.enums.CurrencyType;
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

    @Column(name = "month_salary")
    private BigDecimal monthSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private CurrencyType currencyType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.balance == null) this.balance = BigDecimal.ZERO;
        if (this.monthSalary == null) this.monthSalary = BigDecimal.ZERO;
        if (this.currencyType == null) this.currencyType = CurrencyType.USD;
        if (this.name == null) this.name = "Default profile";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
