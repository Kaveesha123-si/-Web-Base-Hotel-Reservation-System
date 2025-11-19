package com.example.controller;

import com.example.model.*;
import com.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    
    @Autowired
    private UserService userService;
    
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
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElse(null);
        
        if (user != null) {
            model.addAttribute("user", user);
        }
        
        // Get featured content
        List<com.example.model.Package> featuredPackages = packageService.getActivePackages().stream()
                .limit(6)
                .toList();
        
        List<Room> availableRooms = roomService.getAvailableRooms().stream()
                .limit(6)
                .toList();
        
        List<Offer> currentOffers = offerService.getCurrentOffers().stream()
                .limit(3)
                .toList();
        
        model.addAttribute("featuredPackages", featuredPackages);
        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("currentOffers", currentOffers);
        
        return "customer/dashboard";
    }
    
    @GetMapping("/packages")
    public String packages(Model model) {
        model.addAttribute("packages", packageService.getActivePackages());
        return "customer/packages";
    }
    
    @GetMapping("/packages/{id}")
    public String packageDetails(@PathVariable Long id, Model model) {
        com.example.model.Package packageEntity = packageService.getPackageById(id).orElse(null);
        if (packageEntity == null) {
            return "redirect:/customer/packages";
        }
        model.addAttribute("package", packageEntity);
        return "customer/package-details";
    }
    
    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", roomService.getActiveRooms());
        return "customer/rooms";
    }
    
    @GetMapping("/rooms/{id}")
    public String roomDetails(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id).orElse(null);
        if (room == null) {
            return "redirect:/customer/rooms";
        }
        model.addAttribute("room", room);
        return "customer/room-details";
    }
    
    @GetMapping("/activities")
    public String activities(Model model) {
        model.addAttribute("activities", itineraryService.getActiveItineraries());
        return "customer/activities";
    }
    
    @GetMapping("/activities/{id}")
    public String activityDetails(@PathVariable Long id, Model model) {
        Itinerary activity = itineraryService.getItineraryById(id).orElse(null);
        if (activity == null) {
            return "redirect:/customer/activities";
        }
        model.addAttribute("activity", activity);
        return "customer/activity-details";
    }
    
    @GetMapping("/transportation")
    public String transportation(Model model) {
        model.addAttribute("transportations", transportationService.getActiveTransportations());
        return "customer/transportation";
    }
    
    @GetMapping("/transportation/{id}")
    public String transportationDetails(@PathVariable Long id, Model model) {
        Transportation transportation = transportationService.getTransportationById(id).orElse(null);
        if (transportation == null) {
            return "redirect:/customer/transportation";
        }
        model.addAttribute("transportation", transportation);
        return "customer/transportation-details";
    }
    
    @GetMapping("/offers")
    public String offers(Model model) {
        model.addAttribute("offers", offerService.getCurrentOffers());
        return "customer/offers";
    }
    
    @GetMapping("/offers/{id}")
    public String offerDetails(@PathVariable Long id, Model model) {
        Offer offer = offerService.getOfferById(id).orElse(null);
        if (offer == null) {
            return "redirect:/customer/offers";
        }
        model.addAttribute("offer", offer);
        return "customer/offer-details";
    }
    
    @GetMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("faqs", faqService.getActiveFAQs());
        return "customer/faq";
    }
    
    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElse(null);
        
        if (user != null) {
            model.addAttribute("user", user);
        }
        
        return "customer/profile";
    }
    
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user, Authentication authentication) {
        String username = authentication.getName();
        User existingUser = userService.getUserByUsername(username).orElse(null);
        
        if (existingUser != null) {
            existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            userService.updateUser(existingUser);
        }
        
        return "redirect:/customer/profile";
    }
}

