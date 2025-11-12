package com.jstn9.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequestDTO {

    @NotBlank(message = "Username cannot be empty!")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
