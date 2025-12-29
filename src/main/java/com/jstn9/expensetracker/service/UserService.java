package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.auth.RegistrationRequest;
import com.jstn9.expensetracker.dto.auth.UserResponse;
import com.jstn9.expensetracker.exception.EmailAlreadyExistsException;
import com.jstn9.expensetracker.exception.RoleNotFoundException;
import com.jstn9.expensetracker.exception.UsernameAlreadyExistsException;
import com.jstn9.expensetracker.mapper.UserMapper;
import com.jstn9.expensetracker.model.enums.CurrencyType;
import com.jstn9.expensetracker.model.Profile;
import com.jstn9.expensetracker.model.Role;
import com.jstn9.expensetracker.model.User;
import com.jstn9.expensetracker.model.enums.RoleNames;
import com.jstn9.expensetracker.repository.ProfileRepository;
import com.jstn9.expensetracker.repository.RoleRepository;
import com.jstn9.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ProfileRepository profileRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserResponse save(RegistrationRequest user) {

        checkUserDoesNotExist(user);

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleNames.ROLE_USER).orElseThrow(RoleNotFoundException::new);
        newUser.getRoles().add(userRole);

        User savedUser = userRepository.save(newUser);

        createEmptyProfile(savedUser);

        return userMapper.toUserResponse(savedUser);
    }

    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private void createEmptyProfile(User user){
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setName("");
        profile.setBalance(BigDecimal.ZERO);
        profile.setMonthSalary(BigDecimal.ZERO);
        profile.setCurrencyType(CurrencyType.EUR);

        profileRepository.save(profile);
    }

    private void checkUserDoesNotExist(RegistrationRequest user){
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
    }
}
