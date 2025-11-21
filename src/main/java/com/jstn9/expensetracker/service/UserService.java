package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.auth.RegistrationRequest;
import com.jstn9.expensetracker.exception.EmailAlreadyExistsException;
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

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public User save(RegistrationRequest user) {

        //Check if username or email exists
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }

        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }
//        if(userRepository.existsUserByUsername((user.getUsername()))) {
//            throw new UserAlreadyExistsException("User with username " + user.getUsername() + " already exists");
//        }
//        if (userRepository.existsUserByEmail((user.getEmail()))){
//            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
//        }

        //Create new user
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleNames.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        newUser.getRoles().add(userRole);

        User savedUser = userRepository.save(newUser);

        //Create empty profile and save it after user registration
        Profile profile = new Profile();
        profile.setUser(savedUser);
        profile.setName(savedUser.getUsername());
        profile.setBalance(BigDecimal.ZERO);
        profile.setMonthSalary(BigDecimal.ZERO);
        profile.setCurrencyType(CurrencyType.EUR);

        profileRepository.save(profile);

        return savedUser;
    }

    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }


}
