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
    private String charityActionId;
    private double amount;
    private String message;
    private boolean anonymous;
    private PaymentStatus status = PaymentStatus.PENDING;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 