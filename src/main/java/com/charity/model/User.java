package com.charity.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String phoneNumber;
    private String address;
    private String profilePicture;

    private UserRole role = UserRole.DONOR;
    private boolean enabled = true;
    private boolean emailNotifications = true;
    private boolean donationUpdates = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Set<String> donationIds = new HashSet<>();

    public void addDonation(String donationId) {
        donationIds.add(donationId);
    }

    public void removeDonation(String donationId) {
        donationIds.remove(donationId);
    }
} 