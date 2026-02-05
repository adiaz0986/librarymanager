package com.prueba.ezertech.librarymanager.security.config;

import com.prueba.ezertech.librarymanager.user.entity.User;
import com.prueba.ezertech.librarymanager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {

            if (userRepository.findByUsername("admin").isEmpty()) {

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setEnabled(true);

                userRepository.save(admin);

                System.out.println("Usuario admin creado (admin/admin)");
            }
        };
    }
}
