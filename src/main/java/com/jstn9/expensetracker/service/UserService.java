package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.auth.RegistrationRequest;
import com.jstn9.expensetracker.exception.EmailAlreadyExistsException;
import com.jstn9.expensetracker.exception.RoleNotFoundException;
import com.jstn9.expensetracker.exception.UsernameAlreadyExistsException;
import com.jstn9.expensetracker.models.enums.CurrencyType;
import com.jstn9.expensetracker.models.Profile;
import com.jstn9.expensetracker.models.Role;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.models.enums.RoleNames;
import com.jstn9.expensetracker.repository.ProfileRepository;
import com.jstn9.expensetracker.repository.RoleRepository;
import com.jstn9.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public User save(RegistrationRequest user) {

        if (userRepository.existsUserByUsername(user.getUsername())) {
            log.warn("User with username {} already exists", user.getUsername());
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsUserByEmail(user.getEmail())) {
            log.warn("User with email {} already exists", user.getEmail());
            throw new EmailAlreadyExistsException();
        }

        log.info("Start registration user with the username: {}, email: {}",
                user.getUsername(),  user.getEmail());

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleNames.ROLE_USER).orElseThrow(() -> {
            log.error("Role {} not found", RoleNames.ROLE_USER);
            return new RoleNotFoundException();
        });
        newUser.getRoles().add(userRole);

        User savedUser = userRepository.save(newUser);
        log.info("User saved successfully with id: {}, username: {}",
                savedUser.getId(), savedUser.getUsername());

        //Create empty profile and save it after user registration
        Profile profile = new Profile();
        profile.setUser(savedUser);
        profile.setName("");
        profile.setBalance(BigDecimal.ZERO);
        profile.setMonthSalary(BigDecimal.ZERO);
        profile.setCurrencyType(CurrencyType.EUR);

        profileRepository.save(profile);
        log.info("Empty profile created for user with id: {}, username: {}",
                savedUser.getId(), savedUser.getUsername());

        log.info("Registration completed successfully for user with id: {}, username: {}",
                savedUser.getId(), savedUser.getUsername());
        return savedUser;
    }

    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        log.debug("Get current user with username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User with username {} not found", username);
                    return new UsernameNotFoundException(username);
                });
    }
}
