package com.jstn9.expensetracker.controller;

import com.jstn9.expensetracker.dto.auth.*;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.service.AuthService;
import com.jstn9.expensetracker.service.UserService;
import com.jstn9.expensetracker.util.MapperUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegistrationRequest user) {
        User savedUser = userService.save(user);
        UserResponse response = MapperUtil.toUserResponse(savedUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        LoginResponse response = authService.refreshToken(refreshRequest.getRefreshToken());
        return ResponseEntity.ok(response);
    }

}
