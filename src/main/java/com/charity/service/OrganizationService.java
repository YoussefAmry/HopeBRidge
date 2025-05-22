package com.charity.service;

import com.charity.model.Organization;
import com.charity.model.OrganizationStatus;
import com.charity.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Transactional
    public Organization saveOrganization(Organization organization) {
        // Set timestamps
        if (organization.getCreatedAt() == null) {
            organization.setCreatedAt(LocalDateTime.now());
        }
        organization.setUpdatedAt(LocalDateTime.now());
        
        return organizationRepository.save(organization);
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<Organization> getOrganizationById(String id) {
        return organizationRepository.findById(id);
    }

    public Optional<Organization> getOrganizationByEmail(String email) {
        return organizationRepository.findByEmail(email);
    }

    @Transactional
    public void deleteOrganization(String id) {
        organizationRepository.deleteById(id);
    }

    public List<Organization> getActiveOrganizations() {
        return organizationRepository.findByStatus(OrganizationStatus.ACTIVE);
    }
} 