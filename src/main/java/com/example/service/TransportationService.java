package com.example.service;

import com.example.model.Transportation;
import com.example.repository.TransportationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TransportationService {
    
    @Autowired
    private TransportationRepository transportationRepository;
    
    public List<Transportation> getAllTransportations() {
        return transportationRepository.findAll();
    }
    
    public List<Transportation> getActiveTransportations() {
        return transportationRepository.findByActiveTrueOrderByCreatedAtDesc();
    }
    
    public Optional<Transportation> getTransportationById(Long id) {
        return transportationRepository.findById(id);
    }
    
    public Transportation saveTransportation(Transportation transportation) {
        return transportationRepository.save(transportation);
    }
    
    public Transportation updateTransportation(Transportation transportation) {
        return transportationRepository.save(transportation);
    }
    
    public void deleteTransportation(Long id) {
        transportationRepository.deleteById(id);
    }
    
    public List<Transportation> getTransportationsByType(Transportation.TransportationType type) {
        return transportationRepository.findByTypeAndActiveTrue(type);
    }
    
    public List<Transportation> searchTransportations(String serviceName) {
        return transportationRepository.findByServiceNameContainingIgnoreCaseAndActiveTrue(serviceName);
    }
}

