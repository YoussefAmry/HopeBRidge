package com.charity.controller;

import com.charity.model.User;
import com.charity.service.CustomUserDetails;
import com.charity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // Get the User object from CustomUserDetails
        User user = userDetails.getUser();
        model.addAttribute("user", user);
        return "profile"; // Assuming you have a profile.html template
    }

    // Placeholder for updating profile
    @PostMapping
    public String updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Get the existing user
            User user = userDetails.getUser();

            // Update user details
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            user.setAddress(address);

            // Call UserService method to handle file upload and update user's profilePicture field
            if (profilePicture != null && !profilePicture.isEmpty()) {
                userService.updateProfilePicture(user, profilePicture);
            }

            // Call UserService to save updated user details
            userService.updateUser(user);

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
        }

        return "redirect:/profile";
    }
} 