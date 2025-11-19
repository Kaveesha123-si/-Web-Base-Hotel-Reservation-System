package com.example.repository;

import com.example.model.Transportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransportationRepository extends JpaRepository<Transportation, Long> {
    
    List<Transportation> findByActiveTrueOrderByCreatedAtDesc();
    
    List<Transportation> findByTypeAndActiveTrue(Transportation.TransportationType type);
    
    List<Transportation> findByServiceNameContainingIgnoreCaseAndActiveTrue(String serviceName);
}

