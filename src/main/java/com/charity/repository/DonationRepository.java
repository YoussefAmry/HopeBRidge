package com.charity.repository;

import com.charity.model.Donation;
import com.charity.model.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRepository extends MongoRepository<Donation, String> {
    List<Donation> findByUserId(String userId);
    List<Donation> findByCharityActionId(String charityActionId);
    List<Donation> findByStatus(PaymentStatus status);
} 