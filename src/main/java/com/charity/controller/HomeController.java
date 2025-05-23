package com.charity.controller;

import com.charity.model.User;
import com.charity.service.CharityActionService;
import com.charity.service.OrganizationService;
import com.charity.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private CharityActionService charityActionService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/")
    public String home(Model model) {
        // Get the authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            model.addAttribute("user", userDetails.getUser());
        }

        // Get featured actions
        model.addAttribute("featuredActions", charityActionService.getFeaturedCharityActions());
        
        // Create a map of organization IDs to organizations for easy lookup
        Map<String, Object> organizationsMap = new HashMap<>();
        organizationService.getAllOrganizations().forEach(org -> 
            organizationsMap.put(org.getId(), org)
        );
        model.addAttribute("organizationsMap", organizationsMap);
        
        return "home";
    }
} 