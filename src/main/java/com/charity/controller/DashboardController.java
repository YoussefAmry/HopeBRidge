package com.charity.controller;

import com.charity.model.User;
import com.charity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Get current user
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);

        // Add statistics (placeholder values for now)
        model.addAttribute("totalDonations", 0);
        model.addAttribute("activeActions", 0);
        model.addAttribute("impactScore", 0);
        model.addAttribute("unreadNotifications", 0);

        // Add recent activities (placeholder for now)
        List<Object> recentActivities = new ArrayList<>();
        model.addAttribute("recentActivities", recentActivities);

        // Add recommended actions (placeholder for now)
        List<Object> recommendedActions = new ArrayList<>();
        model.addAttribute("recommendedActions", recommendedActions);

        return "dashboard";
    }
} 