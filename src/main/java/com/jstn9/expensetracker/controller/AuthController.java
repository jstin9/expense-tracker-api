package com.jstn9.expensetracker.controller;

import com.jstn9.expensetracker.dto.UserLoginRequestDTO;
import com.jstn9.expensetracker.dto.UserRegistrationRequestDTO;
import com.jstn9.expensetracker.service.AuthService;
import com.jstn9.expensetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequestDTO user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequestDTO loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "username", loginRequest.getUsername()
        ));
    }

}
