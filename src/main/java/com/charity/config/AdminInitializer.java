package com.charity.config;

import com.charity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            // Create admin account with these credentials
            String adminEmail = "admin@charity.com";
            String adminPassword = "admin123"; // This will be encoded
            String adminFirstName = "Admin";
            String adminLastName = "User";

            // Encode the password
            String encodedPassword = passwordEncoder.encode(adminPassword);

            // Create the admin account
            userService.createAdminAccount(adminEmail, encodedPassword, adminFirstName, adminLastName);
            System.out.println("Admin account created successfully!");
        } catch (RuntimeException e) {
            // If admin already exists, just log it
            System.out.println("Admin account already exists or error occurred: " + e.getMessage());
        }
    }
} 