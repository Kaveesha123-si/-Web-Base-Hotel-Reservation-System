package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transportations")
public class Transportation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Service name is required")
    private String serviceName;
    
    @NotBlank(message = "Description is required")
    @Column(length = 1000)
    private String description;
    
    @Enumerated(EnumType.STRING)
    private TransportationType type;
    
    private String fromLocation;
    
    private String toLocation;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be non-negative")
    private BigDecimal price;
    
    private String duration; // e.g., "30 minutes", "2 hours"
    
    private Integer capacity; // Maximum number of passengers
    
    private String imageUrl;
    
    private boolean active = true;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public Transportation() {}
    
    public Transportation(String serviceName, String description, TransportationType type, String fromLocation, String toLocation, BigDecimal price) {
        this.serviceName = serviceName;
        this.description = description;
        this.type = type;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.price = price;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public TransportationType getType() {
        return type;
    }
    
    public void setType(TransportationType type) {
        this.type = type;
    }
    
    public String getFromLocation() {
        return fromLocation;
    }
    
    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }
    
    public String getToLocation() {
        return toLocation;
    }
    
    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
    
    public enum TransportationType {
        AIRPORT_PICKUP("Airport Pickup"),
        AIRPORT_DROPOFF("Airport Drop-off"),
        CITY_TOUR("City Tour"),
        INTERCITY("Intercity Travel"),
        RENTAL_CAR("Car Rental"),
        TAXI("Taxi Service"),
        BUS("Bus Service");
        
        private final String displayName;
        
        TransportationType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}

