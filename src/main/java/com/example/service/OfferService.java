package com.example.service;

import com.example.model.Offer;
import com.example.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    
    @Autowired
    private OfferRepository offerRepository;
    
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }
    
    public List<Offer> getActiveOffers() {
        return offerRepository.findByActiveTrueOrderByCreatedAtDesc();
    }
    
    public List<Offer> getCurrentOffers() {
        LocalDate today = LocalDate.now();
        return offerRepository.findByValidFromLessThanEqualAndValidToGreaterThanEqualAndActiveTrue(today, today);
    }
    
    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }
    
    public Offer saveOffer(Offer offer) {
        return offerRepository.save(offer);
    }
    
    public Offer updateOffer(Offer offer) {
        return offerRepository.save(offer);
    }
    
    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }
    
    public List<Offer> getOffersByType(Offer.OfferType type) {
        return offerRepository.findByTypeAndActiveTrue(type);
    }
    
    public Optional<Offer> getOfferByPromoCode(String promoCode) {
        return offerRepository.findByPromoCodeAndActiveTrue(promoCode).stream().findFirst();
    }
}

