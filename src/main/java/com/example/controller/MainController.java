package com.example.controller;

import com.example.model.*;
import com.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    
    @Autowired
    private PackageService packageService;
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private ItineraryService itineraryService;
    
    @Autowired
    private TransportationService transportationService;
    
    @Autowired
    private OfferService offerService;
    
    @Autowired
    private FAQService faqService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String home(Model model) {
        // Get featured content for home page
        List<com.example.model.Package> featuredPackages = packageService.getActivePackages().stream()
                .limit(3)
                .toList();
        
        List<Room> featuredRooms = roomService.getActiveRooms().stream()
                .limit(3)
                .toList();
        
        List<Itinerary> featuredActivities = itineraryService.getActiveItineraries().stream()
                .limit(3)
                .toList();
        
        List<Transportation> featuredTransportations = transportationService.getActiveTransportations().stream()
                .limit(3)
                .toList();
        
        List<Offer> activeOffers = offerService.getActiveOffers().stream()
                .limit(3)
                .toList();
        
        List<FAQ> featuredFAQs = faqService.getActiveFAQs().stream()
                .limit(4)
                .toList();
        
        model.addAttribute("featuredPackages", featuredPackages);
        model.addAttribute("featuredRooms", featuredRooms);
        model.addAttribute("featuredActivities", featuredActivities);
        model.addAttribute("featuredTransportations", featuredTransportations);
        model.addAttribute("activeOffers", activeOffers);
        model.addAttribute("featuredFAQs", featuredFAQs);
        
        return "home";
    }
    
    @GetMapping("/refresh")
    public String refreshHome() {
        // This endpoint can be called to refresh the home page data
        return "redirect:/";
    }
    
    
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }
}
