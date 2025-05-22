package com.charity.service;

import com.charity.model.User;
import com.charity.model.UserRole;
import com.charity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Define the upload directory for profile pictures
    private final Path rootLocation = Paths.get("uploads/profile-pictures");

    public UserService() {
        // Create the upload directory if it doesn't exist
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Transactional
    public User registerUser(User user, String encodedPassword) {
        // Validate required fields
        if (!StringUtils.hasText(user.getEmail()) || !StringUtils.hasText(user.getPassword())) {
            throw new RuntimeException("Email and password are required");
        }

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Set encoded password
        user.setPassword(encodedPassword);

        // Set default role if not specified
        if (user.getRole() == null) {
            user.setRole(UserRole.DONOR);
        }

        // Set timestamps
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Enable user by default
        user.setEnabled(true);

        // Save user
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        // You can add validation here if needed
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User updateProfilePicture(User user, MultipartFile file) throws IOException {
        // Generate a unique filename
        String filename = user.getId() + "_profile" + 
                          file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        
        // Save the file to the upload directory
        Path destinationFile = this.rootLocation.resolve(
                Paths.get(filename))
                .normalize().toAbsolutePath();
        
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, 
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        // Update the user's profile picture path
        user.setProfilePicture("/uploads/profile-pictures/" + filename); // Store a relative path
        return userRepository.save(user);
    }

    @Transactional
    public User createAdminAccount(String email, String password, String firstName, String lastName) {
        // Check if admin already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Admin account already exists with this email");
        }

        // Create new user
        User admin = new User();
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setRole(UserRole.SUPER_ADMIN);
        admin.setEnabled(true);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());

        // Save admin
        return userRepository.save(admin);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
} 