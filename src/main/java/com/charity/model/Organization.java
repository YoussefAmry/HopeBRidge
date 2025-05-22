package com.charity.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "organizations")
public class Organization {
    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String description;
    private String phoneNumber;
    private String address;
    private String logoUrl;

    private OrganizationStatus status = OrganizationStatus.PENDING;

    private Set<String> adminIds = new HashSet<>();
    private Set<String> actionIds = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void addAdmin(String userId) {
        adminIds.add(userId);
    }

    public void removeAdmin(String userId) {
        adminIds.remove(userId);
    }

    public void addAction(String actionId) {
        actionIds.add(actionId);
    }

    public void removeAction(String actionId) {
        actionIds.remove(actionId);
    }
} 