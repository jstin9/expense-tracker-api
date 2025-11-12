package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.UserLoginRequestDTO;
import com.jstn9.expensetracker.models.Role;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.repository.UserRepository;
import com.jstn9.expensetracker.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(UserLoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return jwtTokenProvider.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getRoles().stream()
                                .map(Role::getName)
                                .toArray(String[]::new))
                        .build()
        );
    }
}
