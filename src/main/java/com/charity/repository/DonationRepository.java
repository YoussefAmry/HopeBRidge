package com.charity.repository;

import com.charity.model.Donation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRepository extends MongoRepository<Donation, String> {
    List<Donation> findByUserId(String userId);
    List<Donation> findByActionId(String actionId);
    List<Donation> findByOrganizationId(String organizationId);
} 