package com.kaamSet.backend.config;

import com.kaamSet.backend.model.Role;
import com.kaamSet.backend.model.User;
import com.kaamSet.backend.model.WorkerAvailability;
import com.kaamSet.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {

        return args -> {

            // Check if admin already exists
            if (userRepository.findByEmailIgnoreCase("admin@kaamSet.com").isEmpty()) {

                User admin = java.util.Objects.requireNonNull(
                        User.builder()
                        .fullName("Super Admin")
                        .email("admin@kaamSet.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .active(true)
                        .area("Default Area")          // Required field
                        .city("Default City")          // Required field
                        .pincode("000000")             // Required field
                        .availability(WorkerAvailability.OFFLINE)       // Optional default
                        .latitude(0.0)                 // Optional, default to 0
                        .longitude(0.0)                // Optional, default to 0
                        .build()
                );

                userRepository.save(admin);

                System.out.println("✅ ADMIN USER CREATED");
            } else {
                System.out.println("ℹ️ Admin already exists, skipping creation.");
            }
        };
    }
}
