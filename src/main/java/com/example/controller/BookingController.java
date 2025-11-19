package com.example.controller;

import com.example.model.*;
import com.example.service.BookingService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/booking")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/form")
    public String showBookingForm(@RequestParam(required = false) String type, 
                                 @RequestParam(required = false) Long id, 
                                 Model model, 
                                 RedirectAttributes redirectAttributes) {
        
        // Check if user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getName().equals("anonymousUser")) {
            redirectAttributes.addFlashAttribute("error", "Please login to make a booking");
            return "redirect:/login";
        }
        
        // Validate parameters
        if (type == null || type.isEmpty() || type.equals("null")) {
            redirectAttributes.addFlashAttribute("error", "Invalid booking type. Please select a valid package, room, or offer.");
            return "redirect:/";
        }
        
        if (id == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid item ID. Please select a valid package, room, or offer.");
            return "redirect:/";
        }
        
        // Get current user
        User currentUser = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Booking booking = new Booking();
        booking.setUser(currentUser);
        
        try {
            switch (type.toLowerCase()) {
                case "package":
                    Optional<com.example.model.Package> packageEntity = bookingService.getPackageById(id);
                    if (packageEntity.isPresent()) {
                        com.example.model.Package pkg = packageEntity.get();
                        booking.setPackageEntity(pkg);
                        booking.setTotalAmount(pkg.getPrice());
                        model.addAttribute("packageDetails", pkg);
                        model.addAttribute("bookingType", "package");
                    } else {
                        redirectAttributes.addFlashAttribute("error", "Package not found");
                        return "redirect:/";
                    }
                    break;
                    
                case "room":
                    Optional<Room> room = bookingService.getRoomById(id);
                    if (room.isPresent()) {
                        Room roomEntity = room.get();
                        booking.setRoom(roomEntity);
                        booking.setTotalAmount(roomEntity.getPricePerNight());
                        model.addAttribute("roomDetails", roomEntity);
                        model.addAttribute("bookingType", "room");
                    } else {
                        redirectAttributes.addFlashAttribute("error", "Room not found");
                        return "redirect:/";
                    }
                    break;
                    
                case "offer":
                    Optional<Offer> offer = bookingService.getOfferById(id);
                    if (offer.isPresent()) {
                        Offer offerEntity = offer.get();
                        // For offers, set a default base price that will be recalculated based on dates
                        BigDecimal basePrice = BigDecimal.valueOf(1000); // Default base price
                        BigDecimal discountedPrice = bookingService.calculateOfferPrice(offerEntity, basePrice);
                        booking.setTotalAmount(discountedPrice);
                        model.addAttribute("offerDetails", offerEntity);
                        model.addAttribute("bookingType", "offer");
                        model.addAttribute("discountPercentage", offerEntity.getDiscountPercentage());
                    } else {
                        redirectAttributes.addFlashAttribute("error", "Offer not found");
                        return "redirect:/";
                    }
                    break;
                    
                default:
                    redirectAttributes.addFlashAttribute("error", "Invalid booking type");
                    return "redirect:/";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading booking details: " + e.getMessage());
            return "redirect:/";
        }
        
        model.addAttribute("booking", booking);
        model.addAttribute("minDate", LocalDate.now().plusDays(1));
        model.addAttribute("originalId", id);
        return "booking/form";
    }
    
    @PostMapping("/create")
    public String createBooking(@Valid @ModelAttribute Booking booking, 
                               BindingResult bindingResult,
                               @RequestParam(required = false) String type,
                               @RequestParam(required = false) Long id,
                               RedirectAttributes redirectAttributes) {
        
        System.out.println("=== BOOKING CREATE DEBUG ===");
        System.out.println("Type: " + type);
        System.out.println("ID: " + id);
        System.out.println("Booking: " + booking);
        System.out.println("Check-in: " + booking.getCheckInDate());
        System.out.println("Check-out: " + booking.getCheckOutDate());
        System.out.println("Total Amount: " + booking.getTotalAmount());
        System.out.println("User: " + (booking.getUser() != null ? booking.getUser().getUsername() : "NULL"));
        System.out.println("Package: " + (booking.getPackageEntity() != null ? booking.getPackageEntity().getName() : "NULL"));
        System.out.println("Room: " + (booking.getRoom() != null ? booking.getRoom().getRoomType() : "NULL"));
        System.out.println("===========================");
        
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            System.out.println("=== VALIDATION ERRORS ===");
            bindingResult.getAllErrors().forEach(error -> {
                if (error instanceof org.springframework.validation.FieldError) {
                    org.springframework.validation.FieldError fieldError = (org.springframework.validation.FieldError) error;
                    System.out.println("Field: " + fieldError.getField());
                    System.out.println("Error: " + fieldError.getDefaultMessage());
                    System.out.println("Rejected Value: " + fieldError.getRejectedValue());
                } else {
                    System.out.println("Error: " + error.getDefaultMessage());
                }
                System.out.println("---");
            });
            System.out.println("========================");
            
            // Create detailed error message
            StringBuilder errorMessage = new StringBuilder("Please fix the following errors: ");
            bindingResult.getAllErrors().forEach(error -> 
                errorMessage.append(error.getDefaultMessage()).append("; ")
            );
            
            redirectAttributes.addFlashAttribute("error", errorMessage.toString());
            return "redirect:/booking/form?type=" + type + "&id=" + id;
        }
        
        try {
            // Custom validation for dates
            if (booking.getCheckInDate() != null && booking.getCheckInDate().isBefore(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("error", "Check-in date must be today or in the future.");
                return "redirect:/booking/form?type=" + type + "&id=" + id;
            }
            
            if (booking.getCheckOutDate() != null && booking.getCheckOutDate().isBefore(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("error", "Check-out date must be today or in the future.");
                return "redirect:/booking/form?type=" + type + "&id=" + id;
            }
            
            if (booking.getCheckInDate() != null && booking.getCheckOutDate() != null && 
                booking.getCheckOutDate().isBefore(booking.getCheckInDate())) {
                redirectAttributes.addFlashAttribute("error", "Check-out date must be after check-in date.");
                return "redirect:/booking/form?type=" + type + "&id=" + id;
            }
            
            // Get current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userService.getUserByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            booking.setUser(currentUser);
            
            // Ensure totalAmount is set to avoid validation errors
            if (booking.getTotalAmount() == null) {
                booking.setTotalAmount(BigDecimal.valueOf(1000)); // Default amount
            }
            
            // Clear any existing entity references to avoid conflicts
            booking.setPackageEntity(null);
            booking.setRoom(null);
            booking.setOffer(null);
            
            // Reload the package/room/offer based on type and id
            if (type != null && id != null) {
                switch (type.toLowerCase()) {
                    case "package":
                        Optional<com.example.model.Package> packageEntity = bookingService.getPackageById(id);
                        if (packageEntity.isPresent()) {
                            booking.setPackageEntity(packageEntity.get());
                        }
                        break;
                    case "room":
                        Optional<Room> room = bookingService.getRoomById(id);
                        if (room.isPresent()) {
                            booking.setRoom(room.get());
                        }
                        break;
                    case "offer":
                        Optional<Offer> offer = bookingService.getOfferById(id);
                        if (offer.isPresent()) {
                            booking.setOffer(offer.get());
                        }
                        break;
                }
            }
            
            // Recalculate total amount based on dates
            if (booking.getCheckInDate() != null && booking.getCheckOutDate() != null) {
                BigDecimal totalAmount = BigDecimal.ZERO;
                
                if (booking.getPackageEntity() != null) {
                    totalAmount = bookingService.calculatePackagePrice(
                        booking.getPackageEntity(), 
                        booking.getCheckInDate(), 
                        booking.getCheckOutDate()
                    );
                } else if (booking.getRoom() != null) {
                    totalAmount = bookingService.calculateRoomPrice(
                        booking.getRoom(), 
                        booking.getCheckInDate(), 
                        booking.getCheckOutDate()
                    );
                } else if (type != null && type.equals("offer") && id != null) {
                    // For offers, calculate based on number of nights with a base price
                    long nights = java.time.temporal.ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
                    if (nights > 0) {
                        BigDecimal basePricePerNight = BigDecimal.valueOf(1000); // Base price per night
                        totalAmount = basePricePerNight.multiply(BigDecimal.valueOf(nights));
                        
                        // Apply offer discount
                        Optional<Offer> offer = bookingService.getOfferById(id);
                        if (offer.isPresent()) {
                            totalAmount = bookingService.calculateOfferPrice(offer.get(), totalAmount);
                        }
                    }
                }
                
                booking.setTotalAmount(totalAmount);
            }
            
            System.out.println("=== CREATING BOOKING ===");
            System.out.println("User ID: " + (booking.getUser() != null ? booking.getUser().getId() : "NULL"));
            System.out.println("Package ID: " + (booking.getPackageEntity() != null ? booking.getPackageEntity().getId() : "NULL"));
            System.out.println("Room ID: " + (booking.getRoom() != null ? booking.getRoom().getId() : "NULL"));
            System.out.println("Total Amount: " + booking.getTotalAmount());
            System.out.println("========================");
            
            Booking savedBooking = bookingService.createBooking(booking);
            System.out.println("=== BOOKING CREATED ===");
            System.out.println("New Booking ID: " + savedBooking.getId());
            System.out.println("======================");
            
            redirectAttributes.addFlashAttribute("success", "Booking created successfully! Booking ID: " + savedBooking.getId());
            return "redirect:/booking/confirmation/" + savedBooking.getId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating booking: " + e.getMessage());
            return "redirect:/booking/form?type=" + type + "&id=" + id;
        }
    }
    
    @GetMapping("/confirmation/{id}")
    public String showConfirmation(@PathVariable Long id, Model model) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "booking/confirmation";
        } else {
            return "redirect:/";
        }
    }
    
    @GetMapping("/my-bookings")
    public String showMyBookings(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getName().equals("anonymousUser")) {
            return "redirect:/login";
        }
        
        User currentUser = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("bookings", bookingService.getBookingsByUser(currentUser));
        return "booking/my-bookings";
    }
}
