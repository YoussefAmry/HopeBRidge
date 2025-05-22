package com.charity.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "charity_actions")
public class CharityAction {
    @Id
    private String id;

    @Indexed
    private String title;
    private String description;
    private String imageUrl;
    private double targetAmount;
    private double currentAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private ActionCategory category;
    private ActionStatus status = ActionStatus.DRAFT;

    @Indexed
    private String organizationId;
    private Set<String> donationIds = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void addDonation(String donationId) {
        donationIds.add(donationId);
    }

    public void removeDonation(String donationId) {
        donationIds.remove(donationId);
    }

    public double getProgress() {
        if (targetAmount == 0) return 0;
        return (currentAmount / targetAmount) * 100;
    }
} 