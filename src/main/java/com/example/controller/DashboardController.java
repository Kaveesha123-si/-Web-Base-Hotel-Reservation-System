package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Check if user has ADMIN role
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            
            if (isAdmin) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/customer/dashboard";
            }
        }
        
        // If not authenticated, redirect to login
        return "redirect:/login";
    }
}
