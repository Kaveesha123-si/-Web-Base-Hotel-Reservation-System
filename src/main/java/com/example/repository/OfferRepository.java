package com.example.repository;

import com.example.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    
    List<Offer> findByActiveTrueOrderByCreatedAtDesc();
    
    List<Offer> findByTypeAndActiveTrue(Offer.OfferType type);
    
    List<Offer> findByValidFromLessThanEqualAndValidToGreaterThanEqualAndActiveTrue(LocalDate validFrom, LocalDate validTo);
    
    List<Offer> findByPromoCodeAndActiveTrue(String promoCode);
}

