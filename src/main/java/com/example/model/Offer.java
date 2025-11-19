package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
public class Offer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Offer title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    @Column(length = 1000)
    private String description;
    
    @NotNull(message = "Discount percentage is required")
    @Min(value = 0, message = "Discount percentage must be at least 0")
    @Max(value = 100, message = "Discount percentage must not exceed 100")
    private Integer discountPercentage;
    
    @DecimalMin(value = "0.0", message = "Minimum amount must be non-negative")
    private BigDecimal minimumAmount;
    
    private LocalDate validFrom;
    
    private LocalDate validTo;
    
    private String imageUrl;
    
    @Enumerated(EnumType.STRING)
    private OfferType type;
    
    private String promoCode;
    
    private boolean active = true;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public Offer() {}
    
    public Offer(String title, String description, Integer discountPercentage, LocalDate validFrom, LocalDate validTo, OfferType type) {
        this.title = title;
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.type = type;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }
    
    public void setMinimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
    }
    
    public LocalDate getValidFrom() {
        return validFrom;
    }
    
    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }
    
    public LocalDate getValidTo() {
        return validTo;
    }
    
    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public OfferType getType() {
        return type;
    }
    
    public void setType(OfferType type) {
        this.type = type;
    }
    
    public String getPromoCode() {
        return promoCode;
    }
    
    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
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
    
    public enum OfferType {
        EARLY_BIRD("Early Bird"),
        LAST_MINUTE("Last Minute"),
        SEASONAL("Seasonal"),
        PACKAGE("Package Deal"),
        LOYALTY("Loyalty Reward"),
        GROUP("Group Discount");
        
        private final String displayName;
        
        OfferType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}

