package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        
        // Check if username already exists
        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
            return "register";
        }
        
        // Check if email already exists
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email already exists");
            return "register";
        }
        
        // Register the user as a customer
        userService.registerCustomer(user);
        
        return "redirect:/login?registered=true";
    }
}
