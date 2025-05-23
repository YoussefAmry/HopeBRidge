package com.charity.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "donations")
public class Donation {
    @Id
    private String id;
    
    private String userId;
    private String actionId;
    private String actionTitle;
    private String organizationId;
    private String organizationName;
    private double amount;
    private DonationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum DonationStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED
    }
} 