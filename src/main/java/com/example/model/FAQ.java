package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "faqs")
public class FAQ {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Question is required")
    @Column(length = 500)
    private String question;
    
    @NotBlank(message = "Answer is required")
    @Column(length = 2000)
    private String answer;
    
    @Enumerated(EnumType.STRING)
    private Category category;
    
    private Integer displayOrder = 0;
    
    private boolean active = true;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public FAQ() {}
    
    public FAQ(String question, String answer, Category category) {
        this.question = question;
        this.answer = answer;
        this.category = category;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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
    
    public enum Category {
        BOOKING("Booking & Reservations"),
        PAYMENT("Payment & Billing"),
        CANCELLATION("Cancellation & Refunds"),
        AMENITIES("Hotel Amenities"),
        TRANSPORTATION("Transportation"),
        ACTIVITIES("Activities & Tours"),
        GENERAL("General Information");
        
        private final String displayName;
        
        Category(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}

