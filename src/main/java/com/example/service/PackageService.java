package com.example.service;

import com.example.model.Package;
import com.example.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PackageService {
    
    @Autowired
    private PackageRepository packageRepository;
    
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }
    
    public List<Package> getActivePackages() {
        return packageRepository.findByActiveTrueOrderByCreatedAtDesc();
    }
    
    public Optional<Package> getPackageById(Long id) {
        return packageRepository.findById(id);
    }
    
    public Package savePackage(Package packageEntity) {
        return packageRepository.save(packageEntity);
    }
    
    public Package updatePackage(Package packageEntity) {
        return packageRepository.save(packageEntity);
    }
    
    public void deletePackage(Long id) {
        packageRepository.deleteById(id);
    }
    
    public List<Package> getPackagesByType(Package.PackageType type) {
        return packageRepository.findByTypeAndActiveTrue(type);
    }
    
    public List<Package> searchPackages(String name) {
        return packageRepository.findByNameContainingIgnoreCaseAndActiveTrue(name);
    }
}

