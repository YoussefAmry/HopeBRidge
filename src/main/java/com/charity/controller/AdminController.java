package com.charity.controller;

import com.charity.model.CharityAction;
import com.charity.model.Organization;
import com.charity.model.User;
import com.charity.model.UserRole;
import com.charity.service.CharityActionService;
import com.charity.service.OrganizationService;
import com.charity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CharityActionService charityActionService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String adminDashboard(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("actions", charityActionService.getAllCharityActions(pageable));
        
        // Get all organizations as a list
        List<Organization> organizationsList = organizationService.getAllOrganizations();
        model.addAttribute("organizations", organizationsList);
        
        // Create a map of organization IDs to organizations for easy lookup
        Map<String, Organization> organizationsMap = new HashMap<>();
        organizationsList.forEach(org -> 
            organizationsMap.put(org.getId(), org)
        );
        model.addAttribute("organizationsMap", organizationsMap);
        
        // Add users to the model
        List<User> usersList = userService.getAllUsers();
        model.addAttribute("users", usersList);
        
        return "admin/dashboard";
    }

    // --- User Management ---

    @PostMapping("/users")
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        logger.debug("Adding new user: {}", user.getEmail());
        try {
            // Encode password
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            // Save user
            userService.registerUser(user, encodedPassword);
            logger.debug("User added successfully");
            redirectAttributes.addFlashAttribute("success", "User added successfully!");
        } catch (Exception e) {
            logger.error("Error adding user", e);
            redirectAttributes.addFlashAttribute("error", "Error adding user: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/users/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable String id) {
        logger.debug("Deleting user with id: {}", id);
        try {
            userService.deleteUser(id);
            logger.debug("User deleted successfully");
            return "User deleted successfully";
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            return "Error deleting user: " + e.getMessage();
        }
    }

    @PostMapping("/users/{id}/role")
    @ResponseBody
    public String updateUserRole(@PathVariable String id, @RequestParam UserRole role) {
        logger.debug("Updating role for user {} to {}", id, role);
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setRole(role);
            userService.updateUser(user);
            logger.debug("User role updated successfully");
            return "User role updated successfully";
        } catch (Exception e) {
            logger.error("Error updating user role", e);
            return "Error updating user role: " + e.getMessage();
        }
    }

    // --- Organization Management ---

    @GetMapping("/organizations/{id}/edit")
    public String showEditOrganizationForm(@PathVariable String id, Model model) {
        logger.debug("Showing edit organization form for id: {}", id);
        try {
            Organization organization = organizationService.getOrganizationById(id)
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            model.addAttribute("organization", organization);
            return "admin/edit-organization"; // You'll need to create this template
        } catch (Exception e) {
            logger.error("Error showing edit organization form", e);
            model.addAttribute("error", "Error loading organization for editing: " + e.getMessage());
            return "admin/dashboard"; // Redirect back to dashboard on error
        }
    }

    @GetMapping("/organizations/new")
    public String showAddOrganizationForm(Model model) {
        logger.debug("Showing add organization form");
        model.addAttribute("organization", new Organization());
        return "admin/add-organization";
    }

    @PostMapping("/organizations")
    public String addOrganization(@ModelAttribute Organization organization, RedirectAttributes redirectAttributes) {
        logger.debug("Adding new organization: {}", organization.getName());
        try {
            organizationService.saveOrganization(organization);
            logger.debug("Organization added successfully");
            redirectAttributes.addFlashAttribute("success", "Organization added successfully!");
        } catch (Exception e) {
            logger.error("Error adding organization", e);
            redirectAttributes.addFlashAttribute("error", "Error adding organization: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/organizations/{id}")
    @ResponseBody
    public String deleteOrganization(@PathVariable String id) {
        logger.debug("Deleting organization with id: {}", id);
        try {
            organizationService.deleteOrganization(id);
            logger.debug("Organization deleted successfully");
            return "Organization deleted successfully";
        } catch (Exception e) {
            logger.error("Error deleting organization", e);
            return "Error deleting organization: " + e.getMessage();
        }
    }

    // --- Charity Action Management ---

    @GetMapping("/actions/{id}/edit")
    public String showEditCharityActionForm(@PathVariable String id, Model model) {
        logger.debug("Showing edit charity action form for id: {}", id);
        try {
            CharityAction charityAction = charityActionService.getCharityActionById(id)
                    .orElseThrow(() -> new RuntimeException("Charity action not found"));
            model.addAttribute("charityAction", charityAction);
            model.addAttribute("organizations", organizationService.getAllOrganizations());
            return "admin/edit-charity-action"; // You'll need to create this template
        } catch (Exception e) {
            logger.error("Error showing edit charity action form", e);
            model.addAttribute("error", "Error loading charity action for editing: " + e.getMessage());
            return "admin/dashboard"; // Redirect back to dashboard on error
        }
    }

    @GetMapping("/actions/new")
    public String showAddCharityActionForm(Model model) {
        logger.debug("Showing add charity action form");
        model.addAttribute("charityAction", new CharityAction());
        model.addAttribute("organizations", organizationService.getAllOrganizations());
        return "admin/add-charity-action";
    }

    @PostMapping("/actions")
    public String addCharityAction(@ModelAttribute CharityAction charityAction, RedirectAttributes redirectAttributes) {
        logger.debug("Adding new charity action: {}", charityAction.getTitle());
        try {
            charityActionService.saveCharityAction(charityAction);
            logger.debug("Charity action added successfully");
            redirectAttributes.addFlashAttribute("success", "Charity action added successfully!");
        } catch (Exception e) {
            logger.error("Error adding charity action", e);
            redirectAttributes.addFlashAttribute("error", "Error adding charity action: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/actions/{id}")
    @ResponseBody
    public String deleteCharityAction(@PathVariable String id) {
        logger.debug("Deleting charity action with id: {}", id);
        try {
            charityActionService.deleteCharityAction(id);
            logger.debug("Charity action deleted successfully");
            return "Charity action deleted successfully";
        } catch (Exception e) {
            logger.error("Error deleting charity action", e);
            return "Error deleting charity action: " + e.getMessage();
        }
    }

    @PostMapping("/actions/{id}")
    public String updateCharityAction(@PathVariable String id, 
                                    @ModelAttribute CharityAction charityAction,
                                    RedirectAttributes redirectAttributes) {
        logger.debug("Updating charity action with id: {}", id);
        try {
            // Get the existing charity action
            CharityAction existingAction = charityActionService.getCharityActionById(id)
                    .orElseThrow(() -> new RuntimeException("Charity action not found"));

            // Update the fields
            existingAction.setTitle(charityAction.getTitle());
            existingAction.setDescription(charityAction.getDescription());
            existingAction.setOrganizationId(charityAction.getOrganizationId());
            existingAction.setTargetAmount(charityAction.getTargetAmount());
            existingAction.setCurrentAmount(charityAction.getCurrentAmount());
            existingAction.setStatus(charityAction.getStatus());
            existingAction.setStartDate(charityAction.getStartDate());
            existingAction.setEndDate(charityAction.getEndDate());
            
            // Update image URL if provided
            if (charityAction.getImageUrl() != null && !charityAction.getImageUrl().trim().isEmpty()) {
                existingAction.setImageUrl(charityAction.getImageUrl());
            }

            // Save the updated charity action
            charityActionService.saveCharityAction(existingAction);
            logger.debug("Charity action updated successfully");
            redirectAttributes.addFlashAttribute("success", "Charity action updated successfully!");
        } catch (Exception e) {
            logger.error("Error updating charity action", e);
            redirectAttributes.addFlashAttribute("error", "Error updating charity action: " + e.getMessage());
        }
        return "redirect:/admin";
    }
} 