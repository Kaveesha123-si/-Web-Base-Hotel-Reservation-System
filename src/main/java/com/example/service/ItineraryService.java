package com.example.service;

import com.example.model.Itinerary;
import com.example.repository.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ItineraryService {
    
    @Autowired
    private ItineraryRepository itineraryRepository;
    
    public List<Itinerary> getAllItineraries() {
        return itineraryRepository.findAll();
    }
    
    public List<Itinerary> getActiveItineraries() {
        return itineraryRepository.findByActiveTrueOrderByCreatedAtDesc();
    }
    
    public Optional<Itinerary> getItineraryById(Long id) {
        return itineraryRepository.findById(id);
    }
    
    public Itinerary saveItinerary(Itinerary itinerary) {
        return itineraryRepository.save(itinerary);
    }
    
    public Itinerary updateItinerary(Itinerary itinerary) {
        return itineraryRepository.save(itinerary);
    }
    
    public void deleteItinerary(Long id) {
        itineraryRepository.deleteById(id);
    }
    
    public List<Itinerary> getItinerariesByType(Itinerary.ActivityType type) {
        return itineraryRepository.findByTypeAndActiveTrue(type);
    }
    
    public List<Itinerary> searchItineraries(String activityName) {
        return itineraryRepository.findByActivityNameContainingIgnoreCaseAndActiveTrue(activityName);
    }
}

