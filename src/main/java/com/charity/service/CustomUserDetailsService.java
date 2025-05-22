package com.charity.service;

import com.charity.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by email: {}", email);
        
        try {
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.warn("User not found with email: {}", email);
                        return new UsernameNotFoundException("User not found with email: " + email);
                    });

            logger.debug("Found user: {}", user.getEmail());

            if (!user.isEnabled()) {
                logger.warn("User account is disabled: {}", email);
                throw new UsernameNotFoundException("User account is disabled");
            }

            String role = "ROLE_" + user.getRole().name();
            logger.debug("User role: {}", role);

            return new CustomUserDetails(user);
        } catch (Exception e) {
            logger.error("Error loading user by email: " + email, e);
            throw new UsernameNotFoundException("Error loading user: " + e.getMessage());
        }
    }
} 