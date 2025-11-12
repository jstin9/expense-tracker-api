package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.UserRegistrationRequestDTO;
import com.jstn9.expensetracker.exception.UserAlreadyExistsException;
import com.jstn9.expensetracker.models.Role;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.repository.RoleRepository;
import com.jstn9.expensetracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(UserRegistrationRequestDTO user) {

        if(userRepository.existsUserByUsername((user.getUsername()))) {
            throw new UserAlreadyExistsException("User with username " + user.getUsername() + " already exists");
        }

        if (userRepository.existsUserByEmail((user.getEmail()))){
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        newUser.getRoles().add(userRole);

        return userRepository.save(newUser);
    }


}
