package com.charity.repository;

import com.charity.model.CharityAction;
import com.charity.model.ActionCategory;
import com.charity.model.ActionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CharityActionRepository extends MongoRepository<CharityAction, String> {
    List<CharityAction> findByCategory(ActionCategory category);
    List<CharityAction> findByStatus(ActionStatus status);
    List<CharityAction> findByOrganizationId(String organizationId);
    Optional<CharityAction> findByTitle(String title);
    List<CharityAction> findByTargetAmountGreaterThan(double amount);
    List<CharityAction> findByCurrentAmountLessThan(double amount);
    List<CharityAction> findTop4ByOrderByCreatedAtDesc();
    Page<CharityAction> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);
} 