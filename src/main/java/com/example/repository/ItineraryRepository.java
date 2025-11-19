package com.example.repository;

import com.example.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    
    List<Itinerary> findByActiveTrueOrderByCreatedAtDesc();
    
    List<Itinerary> findByTypeAndActiveTrue(Itinerary.ActivityType type);
    
    List<Itinerary> findByActivityNameContainingIgnoreCaseAndActiveTrue(String activityName);
}

