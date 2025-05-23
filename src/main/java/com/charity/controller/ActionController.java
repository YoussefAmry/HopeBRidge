package com.charity.controller;

import com.charity.service.CharityActionService;
import com.charity.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/actions")
public class ActionController {

    @Autowired
    private CharityActionService charityActionService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping
    public String actionsPage(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "9") int size,
            @RequestParam(required = false) String query,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        if (query != null && !query.trim().isEmpty()) {
            model.addAttribute("actions", charityActionService.searchCharityActions(query, pageable));
        } else {
            model.addAttribute("actions", charityActionService.getAllCharityActions(pageable));
        }
        
        return "actions";
    }

    @GetMapping("/{id}/details")
    public String actionDetails(@PathVariable String id, Model model) {
        charityActionService.getCharityActionById(id).ifPresent(action -> {
            model.addAttribute("action", action);
            organizationService.getOrganizationById(action.getOrganizationId())
                .ifPresent(org -> model.addAttribute("organization", org));
        });
        return "action-details";
    }

    @GetMapping("/{id}/donate")
    public String actionDonate(@PathVariable String id, Model model) {
        charityActionService.getCharityActionById(id).ifPresent(action -> {
            model.addAttribute("action", action);
            organizationService.getOrganizationById(action.getOrganizationId())
                .ifPresent(org -> model.addAttribute("organization", org));
        });
        return "action-donate";
    }
} 