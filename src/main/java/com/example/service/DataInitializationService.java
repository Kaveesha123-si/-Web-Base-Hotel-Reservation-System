package com.example.service;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class DataInitializationService implements CommandLineRunner {
    
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
    
    @Override
    public void run(String... args) throws Exception {
        initializeData();
    }
    
    private void initializeData() {
        // Create admin user
        userService.createAdminUser();
        
        // Only create sample data if database is empty
        if (packageService.getAllPackages().isEmpty()) {
            createSamplePackages();
        }
        
        if (roomService.getAllRooms().isEmpty()) {
            createSampleRooms();
        }
        
        if (itineraryService.getAllItineraries().isEmpty()) {
            createSampleItineraries();
        }
        
        if (transportationService.getAllTransportations().isEmpty()) {
            createSampleTransportation();
        }
        
        if (offerService.getAllOffers().isEmpty()) {
            createSampleOffers();
        }
        
        if (faqService.getAllFAQs().isEmpty()) {
            createSampleFAQs();
        }
    }
    
    private void createSamplePackages() {
        com.example.model.Package honeymoonPackage = new com.example.model.Package();
        honeymoonPackage.setName("Romantic Honeymoon Getaway");
        honeymoonPackage.setDescription("Perfect for newlyweds! Enjoy 3 days of luxury with candlelight dinners, spa treatments, and romantic room decorations.");
        honeymoonPackage.setPrice(new BigDecimal("150000"));
        honeymoonPackage.setType(com.example.model.Package.PackageType.HONEYMOON);
        honeymoonPackage.setDuration(3);
        honeymoonPackage.setImageUrl("https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80");
        packageService.savePackage(honeymoonPackage);
        
        com.example.model.Package businessPackage = new com.example.model.Package();
        businessPackage.setName("Business Executive Suite");
        businessPackage.setDescription("Ideal for business travelers. Includes high-speed WiFi, meeting rooms, and airport transfers.");
        businessPackage.setPrice(new BigDecimal("75000"));
        businessPackage.setType(com.example.model.Package.PackageType.BUSINESS);
        businessPackage.setDuration(2);
        businessPackage.setImageUrl("https://images.unsplash.com/photo-1631049307264-da0ec9d70304?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80");
        packageService.savePackage(businessPackage);
        
        com.example.model.Package familyPackage = new com.example.model.Package();
        familyPackage.setName("Family Fun Adventure");
        familyPackage.setDescription("Great for families! Includes kids' activities, family-friendly meals, and entertainment shows.");
        familyPackage.setPrice(new BigDecimal("120000"));
        familyPackage.setType(com.example.model.Package.PackageType.FAMILY);
        familyPackage.setDuration(4);
        familyPackage.setImageUrl("https://images.unsplash.com/photo-1571896349842-33c89424de2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80");
        packageService.savePackage(familyPackage);
    }
    
    private void createSampleRooms() {
        Room deluxeRoom = new Room();
        deluxeRoom.setRoomNumber("101");
        deluxeRoom.setRoomType("Deluxe Room");
        deluxeRoom.setPricePerNight(new BigDecimal("25000"));
        deluxeRoom.setCapacity(2);
        deluxeRoom.setDescription("Spacious room with city view, king-size bed, and modern amenities.");
        deluxeRoom.setAmenities("WiFi, Air Conditioning, Mini Bar, TV, Room Service");
        deluxeRoom.setImageUrl("https://images.unsplash.com/photo-1631049307264-da0ec9d70304?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80");
        roomService.saveRoom(deluxeRoom);
        
        Room suiteRoom = new Room();
        suiteRoom.setRoomNumber("201");
        suiteRoom.setRoomType("Executive Suite");
        suiteRoom.setPricePerNight(new BigDecimal("45000"));
        suiteRoom.setCapacity(4);
        suiteRoom.setDescription("Luxurious suite with separate living area, ocean view, and premium services.");
        suiteRoom.setAmenities("WiFi, Air Conditioning, Mini Bar, TV, Room Service, Balcony, Jacuzzi");
        suiteRoom.setImageUrl("https://images.unsplash.com/photo-1578683010236-d716f9a3f461?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80");
        roomService.saveRoom(suiteRoom);
        
        Room familyRoom = new Room();
        familyRoom.setRoomNumber("301");
        familyRoom.setRoomType("Family Room");
        familyRoom.setPricePerNight(new BigDecimal("35000"));
        familyRoom.setCapacity(6);
        familyRoom.setDescription("Perfect for families with connecting rooms and child-friendly amenities.");
        familyRoom.setAmenities("WiFi, Air Conditioning, Mini Bar, TV, Room Service, Extra Beds");
        familyRoom.setImageUrl("https://images.unsplash.com/photo-1571896349842-33c89424de2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80");
        roomService.saveRoom(familyRoom);
    }
    
    private void createSampleItineraries() {
        Itinerary cityTour = new Itinerary();
        cityTour.setActivityName("Colombo City Tour");
        cityTour.setDescription("Explore the vibrant capital city with visits to historical sites, markets, and cultural landmarks.");
        cityTour.setLocation("Colombo");
        cityTour.setDuration("Half Day");
        cityTour.setPrice(new BigDecimal("5000"));
        cityTour.setType(Itinerary.ActivityType.SIGHTSEEING);
        cityTour.setImageUrl("https://images.unsplash.com/photo-1506905925346-21bda4d32df4?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80");
        itineraryService.saveItinerary(cityTour);
        
        Itinerary beachDay = new Itinerary();
        beachDay.setActivityName("Beach Relaxation");
        beachDay.setDescription("Spend a relaxing day at the beautiful beaches with water sports and beachside dining.");
        beachDay.setLocation("Mount Lavinia Beach");
        beachDay.setDuration("Full Day");
        beachDay.setPrice(new BigDecimal("8000"));
        beachDay.setType(Itinerary.ActivityType.RELAXATION);
        beachDay.setImageUrl("https://images.unsplash.com/photo-1544551763-46a013bb70d5?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80");
        itineraryService.saveItinerary(beachDay);
        
        Itinerary culturalTour = new Itinerary();
        culturalTour.setActivityName("Cultural Heritage Tour");
        culturalTour.setDescription("Discover Sri Lanka's rich cultural heritage with visits to temples, museums, and traditional villages.");
        culturalTour.setLocation("Kandy");
        culturalTour.setDuration("Full Day");
        culturalTour.setPrice(new BigDecimal("12000"));
        culturalTour.setType(Itinerary.ActivityType.CULTURAL);
        culturalTour.setImageUrl("https://images.unsplash.com/photo-1578662996442-48f60103fc96?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80");
        itineraryService.saveItinerary(culturalTour);
    }
    
    private void createSampleTransportation() {
        Transportation airportPickup = new Transportation();
        airportPickup.setServiceName("Airport Pickup Service");
        airportPickup.setDescription("Comfortable transfer from Bandaranaike International Airport to the hotel.");
        airportPickup.setType(Transportation.TransportationType.AIRPORT_PICKUP);
        airportPickup.setFromLocation("BIA Airport");
        airportPickup.setToLocation("SAM Hotel");
        airportPickup.setPrice(new BigDecimal("3000"));
        airportPickup.setDuration("45 minutes");
        airportPickup.setCapacity(4);
        transportationService.saveTransportation(airportPickup);
        
        Transportation cityTour = new Transportation();
        cityTour.setServiceName("City Tour Vehicle");
        cityTour.setDescription("Air-conditioned vehicle with driver for city sightseeing tours.");
        cityTour.setType(Transportation.TransportationType.CITY_TOUR);
        cityTour.setFromLocation("Hotel");
        cityTour.setToLocation("Various Locations");
        cityTour.setPrice(new BigDecimal("15000"));
        cityTour.setDuration("8 hours");
        cityTour.setCapacity(6);
        transportationService.saveTransportation(cityTour);
        
        Transportation intercity = new Transportation();
        intercity.setServiceName("Intercity Travel");
        intercity.setDescription("Comfortable travel to other cities like Kandy, Galle, and Nuwara Eliya.");
        intercity.setType(Transportation.TransportationType.INTERCITY);
        intercity.setFromLocation("Colombo");
        intercity.setToLocation("Various Cities");
        intercity.setPrice(new BigDecimal("25000"));
        intercity.setDuration("3-6 hours");
        intercity.setCapacity(4);
        transportationService.saveTransportation(intercity);
    }
    
    private void createSampleOffers() {
        Offer earlyBird = new Offer();
        earlyBird.setTitle("Early Bird Special");
        earlyBird.setDescription("Book 30 days in advance and get 20% off on all packages!");
        earlyBird.setDiscountPercentage(20);
        earlyBird.setValidFrom(LocalDate.now());
        earlyBird.setValidTo(LocalDate.now().plusMonths(3));
        earlyBird.setType(Offer.OfferType.EARLY_BIRD);
        earlyBird.setPromoCode("EARLY20");
        offerService.saveOffer(earlyBird);
        
        Offer weekendSpecial = new Offer();
        weekendSpecial.setTitle("Weekend Getaway");
        weekendSpecial.setDescription("Special rates for weekend stays with complimentary breakfast.");
        weekendSpecial.setDiscountPercentage(15);
        weekendSpecial.setValidFrom(LocalDate.now());
        weekendSpecial.setValidTo(LocalDate.now().plusMonths(2));
        weekendSpecial.setType(Offer.OfferType.SEASONAL);
        weekendSpecial.setPromoCode("WEEKEND15");
        offerService.saveOffer(weekendSpecial);
        
        Offer groupDiscount = new Offer();
        groupDiscount.setTitle("Group Booking Discount");
        groupDiscount.setDescription("Book for 5 or more rooms and get 25% off!");
        groupDiscount.setDiscountPercentage(25);
        groupDiscount.setMinimumAmount(new BigDecimal("100000"));
        groupDiscount.setValidFrom(LocalDate.now());
        groupDiscount.setValidTo(LocalDate.now().plusMonths(6));
        groupDiscount.setType(Offer.OfferType.GROUP);
        groupDiscount.setPromoCode("GROUP25");
        offerService.saveOffer(groupDiscount);
    }
    
    private void createSampleFAQs() {
        FAQ faq1 = new FAQ();
        faq1.setQuestion("What are the check-in and check-out times?");
        faq1.setAnswer("Check-in time is 2:00 PM and check-out time is 11:00 AM. Early check-in and late check-out may be available upon request.");
        faq1.setCategory(FAQ.Category.BOOKING);
        faq1.setDisplayOrder(1);
        faqService.saveFAQ(faq1);
        
        FAQ faq2 = new FAQ();
        faq2.setQuestion("What payment methods do you accept?");
        faq2.setAnswer("We accept all major credit cards, bank transfers, and cash payments in Sri Lankan Rupees (LKR).");
        faq2.setCategory(FAQ.Category.PAYMENT);
        faq2.setDisplayOrder(2);
        faqService.saveFAQ(faq2);
        
        FAQ faq3 = new FAQ();
        faq3.setQuestion("Is WiFi available throughout the hotel?");
        faq3.setAnswer("Yes, complimentary high-speed WiFi is available in all rooms and public areas of the hotel.");
        faq3.setCategory(FAQ.Category.AMENITIES);
        faq3.setDisplayOrder(3);
        faqService.saveFAQ(faq3);
        
        FAQ faq4 = new FAQ();
        faq4.setQuestion("Do you provide airport transportation?");
        faq4.setAnswer("Yes, we offer airport pickup and drop-off services. Please book in advance for guaranteed availability.");
        faq4.setCategory(FAQ.Category.TRANSPORTATION);
        faq4.setDisplayOrder(4);
        faqService.saveFAQ(faq4);
    }
}

