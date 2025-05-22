package com.charity.service;

import com.charity.model.CharityAction;
import com.charity.model.ActionCategory;
import com.charity.model.ActionStatus;
import com.charity.repository.CharityActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CharityActionService {

    @Autowired
    private CharityActionRepository charityActionRepository;

    @Transactional
    public CharityAction saveCharityAction(CharityAction charityAction) {
        // Set timestamps
        if (charityAction.getCreatedAt() == null) {
            charityAction.setCreatedAt(LocalDateTime.now());
        }
        charityAction.setUpdatedAt(LocalDateTime.now());
        
        return charityActionRepository.save(charityAction);
    }

    public List<CharityAction> getFeaturedCharityActions() {
        return charityActionRepository.findTop4ByOrderByCreatedAtDesc();
    }

    public Page<CharityAction> getAllCharityActions(Pageable pageable) {
        return charityActionRepository.findAll(pageable);
    }

    public Page<CharityAction> searchCharityActions(String query, Pageable pageable) {
        return charityActionRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, pageable);
    }

    public Optional<CharityAction> getCharityActionById(String id) {
        return charityActionRepository.findById(id);
    }

    public Optional<CharityAction> getCharityActionByTitle(String title) {
        return charityActionRepository.findByTitle(title);
    }

    public List<CharityAction> getCharityActionsByCategory(ActionCategory category) {
        return charityActionRepository.findByCategory(category);
    }

    public List<CharityAction> getCharityActionsByStatus(ActionStatus status) {
        return charityActionRepository.findByStatus(status);
    }

    public List<CharityAction> getCharityActionsByOrganizationId(String organizationId) {
        return charityActionRepository.findByOrganizationId(organizationId);
    }

    @Transactional
    public void deleteCharityAction(String id) {
        charityActionRepository.deleteById(id);
    }

    public List<CharityAction> getHighValueActions(double amount) {
        return charityActionRepository.findByTargetAmountGreaterThan(amount);
    }

    public List<CharityAction> getUnderfundedActions(double amount) {
        return charityActionRepository.findByCurrentAmountLessThan(amount);
    }
} 