package com.jstn9.expensetracker.dto.profile;

import com.jstn9.expensetracker.models.enums.CurrencyType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProfileResponse {

    private Long id;

    private String name;

    private BigDecimal balance;

    private BigDecimal monthSalary;

    private CurrencyType currencyType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
