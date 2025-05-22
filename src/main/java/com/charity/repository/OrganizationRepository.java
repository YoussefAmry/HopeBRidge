package com.charity.repository;

import com.charity.model.Organization;
import com.charity.model.OrganizationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends MongoRepository<Organization, String> {
    Optional<Organization> findByEmail(String email);
    List<Organization> findByStatus(OrganizationStatus status);
    boolean existsByEmail(String email);
} 