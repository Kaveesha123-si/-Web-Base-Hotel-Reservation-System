package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "itineraries")
public class Itinerary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Activity name is required")
    private String activityName;
    
    @NotBlank(message = "Description is required")
    @Column(length = 1000)
    private String description;
    
    private String location;
    
    private String duration; // e.g., "2 hours", "Half day", "Full day"
    
    @DecimalMin(value = "0.0", message = "Price must be non-negative")
    private BigDecimal price;
    
    private String imageUrl;
    
    @Enumerated(EnumType.STRING)
    private ActivityType type;
    
    private boolean active = true;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public Itinerary() {}
    
    public Itinerary(String activityName, String description, String location, String duration, BigDecimal price, ActivityType type) {
        this.activityName = activityName;
        this.description = description;
        this.location = location;
        this.duration = duration;
        this.price = price;
        this.type = type;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getActivityName() {
        return activityName;
    }
    
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public ActivityType getType() {
        return type;
    }
    
    public void setType(ActivityType type) {
        this.type = type;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public enum ActivityType {
        SIGHTSEEING("Sightseeing"),
        ADVENTURE("Adventure"),
        CULTURAL("Cultural"),
        RELAXATION("Relaxation"),
        FOOD("Food & Dining"),
        SHOPPING("Shopping"),
        NIGHTLIFE("Nightlife");
        
        private final String displayName;
        
        ActivityType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}

