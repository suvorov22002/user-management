package com.pyramid.usermanagement.core.configs;

import com.pyramid.usermanagement.domain.security.model.ERole;
import com.pyramid.usermanagement.domain.security.model.Role;
import com.pyramid.usermanagement.domain.security.repository.RoleRepository;
import jakarta.servlet.annotation.ServletSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Suvorov Vassilievitch
 * Date: 06/04/2025
 * Time: 23:34
 * Project Name: user-management
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        if (roleRepository.count() == 0) {

            Role adminRole = new Role();
            adminRole.setName(ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName(ERole.ROLE_USER);
            roleRepository.save(userRole);

            System.out.println("Roles initialized successfully");
        }
    }
}
