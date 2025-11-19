package com.example.repository;

import com.example.model.Booking;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserOrderByCreatedAtDesc(User user);
    
    List<Booking> findByStatusOrderByCreatedAtDesc(Booking.BookingStatus status);
    
    List<Booking> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Booking> findByUserAndStatus(User user, Booking.BookingStatus status);
}

