package com.example.controller;

import com.example.model.*;
import com.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
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
    
    @Autowired
    private BookingService bookingService;
    
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Get counts for dashboard
        long packageCount = packageService.getAllPackages().size();
        long roomCount = roomService.getAllRooms().size();
        long userCount = userService.getAllUsers().size();
        long offerCount = offerService.getAllOffers().size();
        long bookingCount = bookingService.getAllBookings().size();
        
        model.addAttribute("packageCount", packageCount);
        model.addAttribute("roomCount", roomCount);
        model.addAttribute("userCount", userCount);
        model.addAttribute("offerCount", offerCount);
        model.addAttribute("bookingCount", bookingCount);
        
        return "admin/dashboard";
    }
    
    // Package Management
    @GetMapping("/packages")
    public String packages(Model model) {
        model.addAttribute("packages", packageService.getAllPackages());
        return "admin/packages";
    }
    
    @GetMapping("/packages/new")
    public String newPackage(Model model) {
        model.addAttribute("packageEntity", new com.example.model.Package());
        return "admin/package-form";
    }
    
    @PostMapping("/packages")
    public String savePackage(@ModelAttribute("packageEntity") com.example.model.Package packageEntity, Model model) {
        try {
            // Check if this is an update (package has ID) or insert (package has no ID)
            if (packageEntity.getId() != null) {
                // This is an update - check if package exists
                Optional<com.example.model.Package> existingPackage = packageService.getPackageById(packageEntity.getId());
                if (existingPackage.isPresent()) {
                    // Update existing package
                    com.example.model.Package existing = existingPackage.get();
                    existing.setName(packageEntity.getName());
                    existing.setType(packageEntity.getType());
                    existing.setPrice(packageEntity.getPrice());
                    existing.setDuration(packageEntity.getDuration());
                    existing.setDescription(packageEntity.getDescription());
                    existing.setImageUrl(packageEntity.getImageUrl());
                    existing.setActive(packageEntity.isActive());
                    packageService.savePackage(existing);
                    return "redirect:/admin/packages?success=Package updated successfully";
                } else {
                    // Package with this ID doesn't exist, treat as new
                    packageEntity.setId(null);
                    packageService.savePackage(packageEntity);
                    return "redirect:/admin/packages?success=Package created successfully";
                }
            } else {
                // This is a new package
                packageService.savePackage(packageEntity);
                return "redirect:/admin/packages?success=Package created successfully";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error saving package: " + e.getMessage());
            model.addAttribute("packageEntity", packageEntity); // Preserve form data
            return "admin/package-form";
        }
    }
    
    @GetMapping("/packages/edit/{id}")
    public String editPackage(@PathVariable Long id, Model model) {
        model.addAttribute("packageEntity", packageService.getPackageById(id).orElse(new com.example.model.Package()));
        return "admin/package-form";
    }
    
    @PostMapping("/packages/delete/{id}")
    public String deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return "redirect:/admin/packages?success=Package deleted successfully";
    }
    
    // Room Management
    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin/rooms";
    }
    
    @GetMapping("/rooms/new")
    public String newRoom(Model model) {
        model.addAttribute("room", new Room());
        // Get existing room numbers to help users avoid duplicates
        List<String> existingRoomNumbers = roomService.getAllRooms().stream()
                .map(Room::getRoomNumber)
                .sorted()
                .collect(java.util.stream.Collectors.toList());
        model.addAttribute("existingRoomNumbers", existingRoomNumbers);
        return "admin/room-form";
    }
    
    @PostMapping("/rooms")
    public String saveRoom(@ModelAttribute Room room, Model model) {
        try {
            // Check if this is an update (room has ID) or insert (room has no ID)
            if (room.getId() != null) {
                // This is an update - check if room exists
                Optional<Room> existingRoom = roomService.getRoomById(room.getId());
                if (existingRoom.isPresent()) {
                    // Check if room number is being changed and if the new number already exists
                    if (!existingRoom.get().getRoomNumber().equals(room.getRoomNumber()) && 
                        roomService.isRoomNumberExistsForUpdate(room.getRoomNumber(), room.getId())) {
                        model.addAttribute("error", "Room number '" + room.getRoomNumber() + "' already exists. Please choose a different room number.");
                        model.addAttribute("room", room); // Preserve form data
                        return "admin/room-form";
                    }
                    
                    // Update existing room
                    Room existing = existingRoom.get();
                    existing.setRoomNumber(room.getRoomNumber());
                    existing.setRoomType(room.getRoomType());
                    existing.setPricePerNight(room.getPricePerNight());
                    existing.setCapacity(room.getCapacity());
                    existing.setDescription(room.getDescription());
                    existing.setAmenities(room.getAmenities());
                    existing.setImageUrl(room.getImageUrl());
                    existing.setStatus(room.getStatus());
                    existing.setActive(room.isActive());
                    roomService.saveRoom(existing);
                    return "redirect:/admin/rooms?success=Room updated successfully";
                } else {
                    // Room with this ID doesn't exist, treat as new
                    room.setId(null);
                    // Check if room number already exists for new room
                    if (roomService.isRoomNumberExists(room.getRoomNumber())) {
                        model.addAttribute("error", "Room number '" + room.getRoomNumber() + "' already exists. Please choose a different room number.");
                        model.addAttribute("room", room); // Preserve form data
                        return "admin/room-form";
                    }
                    roomService.saveRoom(room);
                    return "redirect:/admin/rooms?success=Room created successfully";
                }
            } else {
                // This is a new room - check if room number already exists
                if (roomService.isRoomNumberExists(room.getRoomNumber())) {
                    model.addAttribute("error", "Room number '" + room.getRoomNumber() + "' already exists. Please choose a different room number.");
                    model.addAttribute("room", room); // Preserve form data
                    return "admin/room-form";
                }
                roomService.saveRoom(room);
                return "redirect:/admin/rooms?success=Room created successfully";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error saving room: " + e.getMessage());
            model.addAttribute("room", room); // Preserve form data
            return "admin/room-form";
        }
    }
    
    @GetMapping("/rooms/edit/{id}")
    public String editRoom(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.getRoomById(id).orElse(new Room()));
        // Get existing room numbers to help users avoid duplicates
        List<String> existingRoomNumbers = roomService.getAllRooms().stream()
                .map(Room::getRoomNumber)
                .sorted()
                .collect(java.util.stream.Collectors.toList());
        model.addAttribute("existingRoomNumbers", existingRoomNumbers);
        return "admin/room-form";
    }
    
    @PostMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/admin/rooms?success=Room deleted successfully";
    }
    
    // Itinerary Management
    @GetMapping("/itineraries")
    public String itineraries(Model model) {
        model.addAttribute("itineraries", itineraryService.getAllItineraries());
        return "admin/itineraries";
    }
    
    @GetMapping("/itineraries/new")
    public String newItinerary(Model model) {
        model.addAttribute("itinerary", new Itinerary());
        return "admin/itinerary-form";
    }
    
    @PostMapping("/itineraries")
    public String saveItinerary(@ModelAttribute Itinerary itinerary, Model model) {
        try {
            // Check if this is an update (itinerary has ID) or insert (itinerary has no ID)
            if (itinerary.getId() != null) {
                // This is an update - check if itinerary exists
                Optional<Itinerary> existingItinerary = itineraryService.getItineraryById(itinerary.getId());
                if (existingItinerary.isPresent()) {
                    // Update existing itinerary
                    Itinerary existing = existingItinerary.get();
                    existing.setActivityName(itinerary.getActivityName());
                    existing.setDescription(itinerary.getDescription());
                    existing.setLocation(itinerary.getLocation());
                    existing.setDuration(itinerary.getDuration());
                    existing.setPrice(itinerary.getPrice());
                    existing.setType(itinerary.getType());
                    existing.setImageUrl(itinerary.getImageUrl());
                    existing.setActive(itinerary.isActive());
                    itineraryService.saveItinerary(existing);
                    return "redirect:/admin/itineraries?success=Activity updated successfully";
                } else {
                    // Itinerary with this ID doesn't exist, treat as new
                    itinerary.setId(null);
                    itineraryService.saveItinerary(itinerary);
                    return "redirect:/admin/itineraries?success=Activity created successfully";
                }
            } else {
                // This is a new itinerary
                itineraryService.saveItinerary(itinerary);
                return "redirect:/admin/itineraries?success=Activity created successfully";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error saving activity: " + e.getMessage());
            model.addAttribute("itinerary", itinerary); // Preserve form data
            return "admin/itinerary-form";
        }
    }
    
    @GetMapping("/itineraries/edit/{id}")
    public String editItinerary(@PathVariable Long id, Model model) {
        model.addAttribute("itinerary", itineraryService.getItineraryById(id).orElse(new Itinerary()));
        return "admin/itinerary-form";
    }
    
    @PostMapping("/itineraries/delete/{id}")
    public String deleteItinerary(@PathVariable Long id) {
        itineraryService.deleteItinerary(id);
        return "redirect:/admin/itineraries?success=Activity deleted successfully";
    }
    
    // Transportation Management
    @GetMapping("/transportations")
    public String transportations(Model model) {
        model.addAttribute("transportations", transportationService.getAllTransportations());
        return "admin/transportations";
    }
    
    @GetMapping("/transportations/new")
    public String newTransportation(Model model) {
        model.addAttribute("transportation", new Transportation());
        return "admin/transportation-form";
    }
    
    @PostMapping("/transportations")
    public String saveTransportation(@ModelAttribute Transportation transportation, Model model) {
        try {
            // Check if this is an update (transportation has ID) or insert (transportation has no ID)
            if (transportation.getId() != null) {
                // This is an update - check if transportation exists
                Optional<Transportation> existingTransportation = transportationService.getTransportationById(transportation.getId());
                if (existingTransportation.isPresent()) {
                    // Update existing transportation
                    Transportation existing = existingTransportation.get();
                    existing.setServiceName(transportation.getServiceName());
                    existing.setDescription(transportation.getDescription());
                    existing.setType(transportation.getType());
                    existing.setCapacity(transportation.getCapacity());
                    existing.setFromLocation(transportation.getFromLocation());
                    existing.setToLocation(transportation.getToLocation());
                    existing.setPrice(transportation.getPrice());
                    existing.setDuration(transportation.getDuration());
                    existing.setImageUrl(transportation.getImageUrl());
                    existing.setActive(transportation.isActive());
                    transportationService.saveTransportation(existing);
                    return "redirect:/admin/transportations?success=Transportation updated successfully";
                } else {
                    // Transportation with this ID doesn't exist, treat as new
                    transportation.setId(null);
                    transportationService.saveTransportation(transportation);
                    return "redirect:/admin/transportations?success=Transportation created successfully";
                }
            } else {
                // This is a new transportation
                transportationService.saveTransportation(transportation);
                return "redirect:/admin/transportations?success=Transportation created successfully";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error saving transportation: " + e.getMessage());
            model.addAttribute("transportation", transportation); // Preserve form data
            return "admin/transportation-form";
        }
    }
    
    @GetMapping("/transportations/edit/{id}")
    public String editTransportation(@PathVariable Long id, Model model) {
        model.addAttribute("transportation", transportationService.getTransportationById(id).orElse(new Transportation()));
        return "admin/transportation-form";
    }
    
    @PostMapping("/transportations/delete/{id}")
    public String deleteTransportation(@PathVariable Long id) {
        transportationService.deleteTransportation(id);
        return "redirect:/admin/transportations?success=Transportation deleted successfully";
    }
    
    // Offer Management
    @GetMapping("/offers")
    public String offers(Model model) {
        model.addAttribute("offers", offerService.getAllOffers());
        return "admin/offers";
    }
    
    @GetMapping("/offers/new")
    public String newOffer(Model model) {
        model.addAttribute("offer", new Offer());
        return "admin/offer-form";
    }
    
    @PostMapping("/offers")
    public String saveOffer(@ModelAttribute Offer offer, Model model) {
        try {
            // Check if this is an update (offer has ID) or insert (offer has no ID)
            if (offer.getId() != null) {
                // This is an update - check if offer exists
                Optional<Offer> existingOffer = offerService.getOfferById(offer.getId());
                if (existingOffer.isPresent()) {
                    // Update existing offer
                    Offer existing = existingOffer.get();
                    existing.setTitle(offer.getTitle());
                    existing.setDescription(offer.getDescription());
                    existing.setType(offer.getType());
                    existing.setDiscountPercentage(offer.getDiscountPercentage());
                    existing.setValidFrom(offer.getValidFrom());
                    existing.setValidTo(offer.getValidTo());
                    existing.setActive(offer.isActive());
                    offerService.saveOffer(existing);
                    return "redirect:/admin/offers?success=Offer updated successfully";
                } else {
                    // Offer with this ID doesn't exist, treat as new
                    offer.setId(null);
                    offerService.saveOffer(offer);
                    return "redirect:/admin/offers?success=Offer created successfully";
                }
            } else {
                // This is a new offer
                offerService.saveOffer(offer);
                return "redirect:/admin/offers?success=Offer created successfully";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error saving offer: " + e.getMessage());
            model.addAttribute("offer", offer); // Preserve form data
            return "admin/offer-form";
        }
    }
    
    @GetMapping("/offers/edit/{id}")
    public String editOffer(@PathVariable Long id, Model model) {
        model.addAttribute("offer", offerService.getOfferById(id).orElse(new Offer()));
        return "admin/offer-form";
    }
    
    @PostMapping("/offers/delete/{id}")
    public String deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return "redirect:/admin/offers?success=Offer deleted successfully";
    }
    
    // FAQ Management
    @GetMapping("/faqs")
    public String faqs(Model model) {
        model.addAttribute("faqs", faqService.getAllFAQs());
        return "admin/faqs";
    }
    
    @GetMapping("/faqs/new")
    public String newFAQ(Model model) {
        model.addAttribute("faq", new FAQ());
        return "admin/faq-form";
    }
    
    @PostMapping("/faqs")
    public String saveFAQ(@ModelAttribute FAQ faq, Model model) {
        try {
            // Check if this is an update (faq has ID) or insert (faq has no ID)
            if (faq.getId() != null) {
                // This is an update - check if faq exists
                Optional<FAQ> existingFAQ = faqService.getFAQById(faq.getId());
                if (existingFAQ.isPresent()) {
                    // Update existing faq
                    FAQ existing = existingFAQ.get();
                    existing.setQuestion(faq.getQuestion());
                    existing.setAnswer(faq.getAnswer());
                    existing.setCategory(faq.getCategory());
                    existing.setActive(faq.isActive());
                    faqService.saveFAQ(existing);
                    return "redirect:/admin/faqs?success=FAQ updated successfully";
                } else {
                    // FAQ with this ID doesn't exist, treat as new
                    faq.setId(null);
                    faqService.saveFAQ(faq);
                    return "redirect:/admin/faqs?success=FAQ created successfully";
                }
            } else {
                // This is a new faq
                faqService.saveFAQ(faq);
                return "redirect:/admin/faqs?success=FAQ created successfully";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error saving FAQ: " + e.getMessage());
            model.addAttribute("faq", faq); // Preserve form data
            return "admin/faq-form";
        }
    }
    
    @GetMapping("/faqs/edit/{id}")
    public String editFAQ(@PathVariable Long id, Model model) {
        model.addAttribute("faq", faqService.getFAQById(id).orElse(new FAQ()));
        return "admin/faq-form";
    }
    
    @PostMapping("/faqs/delete/{id}")
    public String deleteFAQ(@PathVariable Long id) {
        faqService.deleteFAQ(id);
        return "redirect:/admin/faqs?success=FAQ deleted successfully";
    }
    
    // Booking Management
    @GetMapping("/bookings")
    public String bookings(@RequestParam(required = false) String status,
                          @RequestParam(required = false) String checkInDate,
                          @RequestParam(required = false) String checkOutDate,
                          Model model,
                          jakarta.servlet.http.HttpServletRequest request) {
        List<Booking> bookings;
        
        if (status != null && !status.isEmpty()) {
            try {
                Booking.BookingStatus bookingStatus = Booking.BookingStatus.valueOf(status.toUpperCase());
                bookings = bookingService.getBookingsByStatus(bookingStatus);
            } catch (IllegalArgumentException e) {
                bookings = bookingService.getAllBookings();
            }
        } else if (checkInDate != null && !checkInDate.isEmpty() && checkOutDate != null && !checkOutDate.isEmpty()) {
            try {
                java.time.LocalDate startDate = java.time.LocalDate.parse(checkInDate);
                java.time.LocalDate endDate = java.time.LocalDate.parse(checkOutDate);
                bookings = bookingService.getBookingsByDateRange(startDate, endDate);
            } catch (Exception e) {
                bookings = bookingService.getAllBookings();
            }
        } else {
            bookings = bookingService.getAllBookings();
        }
        
        model.addAttribute("bookings", bookings);
        return "admin/bookings";
    }
    
    @GetMapping("/bookings/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "admin/booking-details";
        } else {
            return "redirect:/admin/bookings?error=Booking not found";
        }
    }
    
    
    @GetMapping("/bookings/{id}/edit")
    public String editBooking(@PathVariable Long id, Model model) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "admin/booking-form";
        } else {
            return "redirect:/admin/bookings?error=Booking not found";
        }
    }
    
    @PostMapping("/bookings/{id}")
    public String updateBooking(@PathVariable Long id, @ModelAttribute Booking booking) {
        try {
            Optional<Booking> existingBooking = bookingService.getBookingById(id);
            if (existingBooking.isPresent()) {
                Booking existing = existingBooking.get();
                existing.setCheckInDate(booking.getCheckInDate());
                existing.setCheckOutDate(booking.getCheckOutDate());
                existing.setNumberOfGuests(booking.getNumberOfGuests());
                existing.setSpecialRequests(booking.getSpecialRequests());
                existing.setStatus(booking.getStatus());
                existing.setUpdatedAt(java.time.LocalDateTime.now());
                
                // Recalculate total amount
                if (existing.getPackageEntity() != null) {
                    existing.setTotalAmount(bookingService.calculatePackagePrice(
                        existing.getPackageEntity(), 
                        existing.getCheckInDate(), 
                        existing.getCheckOutDate()
                    ));
                } else if (existing.getRoom() != null) {
                    existing.setTotalAmount(bookingService.calculateRoomPrice(
                        existing.getRoom(), 
                        existing.getCheckInDate(), 
                        existing.getCheckOutDate()
                    ));
                }
                
                bookingService.updateBooking(existing);
                return "redirect:/admin/bookings?success=Booking updated successfully";
            } else {
                return "redirect:/admin/bookings?error=Booking not found";
            }
        } catch (Exception e) {
            return "redirect:/admin/bookings?error=Error updating booking: " + e.getMessage();
        }
    }
    
    @PostMapping("/bookings/{id}/status")
    public String updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Optional<Booking> booking = bookingService.getBookingById(id);
            if (booking.isPresent()) {
                Booking existing = booking.get();
                existing.setStatus(Booking.BookingStatus.valueOf(status.toUpperCase()));
                existing.setUpdatedAt(java.time.LocalDateTime.now());
                bookingService.updateBooking(existing);
                return "redirect:/admin/bookings?success=Booking status updated successfully";
            } else {
                return "redirect:/admin/bookings?error=Booking not found";
            }
        } catch (Exception e) {
            return "redirect:/admin/bookings?error=Error updating booking status: " + e.getMessage();
        }
    }
    
    @PostMapping("/bookings/{id}/delete")
    public String deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return "redirect:/admin/bookings?success=Booking deleted successfully";
        } catch (Exception e) {
            return "redirect:/admin/bookings?error=Error deleting booking: " + e.getMessage();
        }
    }
    
}

