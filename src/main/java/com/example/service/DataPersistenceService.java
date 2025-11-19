package com.example.service;

import com.example.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DataPersistenceService {
    
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
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String dataDir = "./data";
    private final String packagesFile = dataDir + "/packages.json";
    private final String roomsFile = dataDir + "/rooms.json";
    private final String itinerariesFile = dataDir + "/itineraries.json";
    private final String transportationsFile = dataDir + "/transportations.json";
    private final String offersFile = dataDir + "/offers.json";
    private final String faqsFile = dataDir + "/faqs.json";
    
    @PostConstruct
    public void loadData() {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Paths.get(dataDir));
            
            // Register JavaTimeModule for LocalDateTime serialization
            objectMapper.registerModule(new JavaTimeModule());
            
            // Clear any empty or corrupted files first
            clearCorruptedFiles();
            
            // Only load data if database is empty (to avoid overriding initialized data)
            if (packageService.getAllPackages().isEmpty()) {
                loadPackages();
                loadRooms();
                loadItineraries();
                loadTransportations();
                loadOffers();
                loadFAQs();
                System.out.println("Data loaded successfully from persistence files");
            } else {
                System.out.println("Database already has data, skipping file loading");
            }
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
    
    private void clearCorruptedFiles() {
        String[] files = {packagesFile, roomsFile, itinerariesFile, transportationsFile, offersFile, faqsFile};
        for (String filePath : files) {
            File file = new File(filePath);
            if (file.exists() && file.length() == 0) {
                file.delete();
                System.out.println("Deleted empty file: " + filePath);
            }
        }
    }
    
    @PreDestroy
    public void saveData() {
        try {
            // Save all data to files
            savePackages();
            saveRooms();
            saveItineraries();
            saveTransportations();
            saveOffers();
            saveFAQs();
            
            System.out.println("Data saved successfully to persistence files");
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    private void loadPackages() throws IOException {
        File file = new File(packagesFile);
        if (file.exists() && file.length() > 0) {
            try {
                List<com.example.model.Package> packages = objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, com.example.model.Package.class));
                for (com.example.model.Package pkg : packages) {
                    // Set ID to null to avoid conflicts
                    pkg.setId(null);
                    packageService.savePackage(pkg);
                }
            } catch (Exception e) {
                System.err.println("Error loading packages from " + packagesFile + ": " + e.getMessage());
                // If file is corrupted, delete it
                file.delete();
            }
        }
    }
    
    private void loadRooms() throws IOException {
        File file = new File(roomsFile);
        if (file.exists() && file.length() > 0) {
            try {
                List<Room> rooms = objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Room.class));
                for (Room room : rooms) {
                    room.setId(null);
                    roomService.saveRoom(room);
                }
            } catch (Exception e) {
                System.err.println("Error loading rooms from " + roomsFile + ": " + e.getMessage());
                file.delete();
            }
        }
    }
    
    private void loadItineraries() throws IOException {
        File file = new File(itinerariesFile);
        if (file.exists() && file.length() > 0) {
            try {
                List<Itinerary> itineraries = objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Itinerary.class));
                for (Itinerary itinerary : itineraries) {
                    itinerary.setId(null);
                    itineraryService.saveItinerary(itinerary);
                }
            } catch (Exception e) {
                System.err.println("Error loading itineraries from " + itinerariesFile + ": " + e.getMessage());
                file.delete();
            }
        }
    }
    
    private void loadTransportations() throws IOException {
        File file = new File(transportationsFile);
        if (file.exists() && file.length() > 0) {
            try {
                List<Transportation> transportations = objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Transportation.class));
                for (Transportation transportation : transportations) {
                    transportation.setId(null);
                    transportationService.saveTransportation(transportation);
                }
            } catch (Exception e) {
                System.err.println("Error loading transportations from " + transportationsFile + ": " + e.getMessage());
                file.delete();
            }
        }
    }
    
    private void loadOffers() throws IOException {
        File file = new File(offersFile);
        if (file.exists() && file.length() > 0) {
            try {
                List<Offer> offers = objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Offer.class));
                for (Offer offer : offers) {
                    offer.setId(null);
                    offerService.saveOffer(offer);
                }
            } catch (Exception e) {
                System.err.println("Error loading offers from " + offersFile + ": " + e.getMessage());
                file.delete();
            }
        }
    }
    
    private void loadFAQs() throws IOException {
        File file = new File(faqsFile);
        if (file.exists() && file.length() > 0) {
            try {
                List<FAQ> faqs = objectMapper.readValue(file, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, FAQ.class));
                for (FAQ faq : faqs) {
                    faq.setId(null);
                    faqService.saveFAQ(faq);
                }
            } catch (Exception e) {
                System.err.println("Error loading FAQs from " + faqsFile + ": " + e.getMessage());
                file.delete();
            }
        }
    }
    
    private void savePackages() throws IOException {
        List<com.example.model.Package> packages = packageService.getAllPackages();
        objectMapper.writeValue(new File(packagesFile), packages);
        System.out.println("Saved " + packages.size() + " packages to " + packagesFile);
    }
    
    private void saveRooms() throws IOException {
        List<Room> rooms = roomService.getAllRooms();
        objectMapper.writeValue(new File(roomsFile), rooms);
        System.out.println("Saved " + rooms.size() + " rooms to " + roomsFile);
    }
    
    private void saveItineraries() throws IOException {
        List<Itinerary> itineraries = itineraryService.getAllItineraries();
        objectMapper.writeValue(new File(itinerariesFile), itineraries);
        System.out.println("Saved " + itineraries.size() + " itineraries to " + itinerariesFile);
    }
    
    private void saveTransportations() throws IOException {
        List<Transportation> transportations = transportationService.getAllTransportations();
        objectMapper.writeValue(new File(transportationsFile), transportations);
        System.out.println("Saved " + transportations.size() + " transportations to " + transportationsFile);
    }
    
    private void saveOffers() throws IOException {
        List<Offer> offers = offerService.getAllOffers();
        objectMapper.writeValue(new File(offersFile), offers);
        System.out.println("Saved " + offers.size() + " offers to " + offersFile);
    }
    
    private void saveFAQs() throws IOException {
        List<FAQ> faqs = faqService.getAllFAQs();
        objectMapper.writeValue(new File(faqsFile), faqs);
        System.out.println("Saved " + faqs.size() + " FAQs to " + faqsFile);
    }
    
    // Method to manually save data (can be called from admin panel)
    public void saveAllData() {
        saveData();
    }
}
