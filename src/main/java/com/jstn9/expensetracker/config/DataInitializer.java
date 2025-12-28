package com.jstn9.expensetracker.config;

import com.jstn9.expensetracker.models.Role;
import com.jstn9.expensetracker.models.enums.RoleNames;
import com.jstn9.expensetracker.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/*
    Initialize default roles to db table
 */

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if(roleRepository.findByName(RoleNames.ROLE_USER).isEmpty()){
            roleRepository.save(new Role(1L, RoleNames.ROLE_USER));
        }
        if(roleRepository.findByName(RoleNames.ROLE_ADMIN).isEmpty()){
            roleRepository.save(new Role(2L, RoleNames.ROLE_ADMIN));
        }
    }
}
