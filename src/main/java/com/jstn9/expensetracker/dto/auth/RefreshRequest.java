package com.jstn9.expensetracker.dto.auth;

import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;
}
