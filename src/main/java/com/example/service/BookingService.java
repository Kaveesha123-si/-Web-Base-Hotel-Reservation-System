package com.example.service;

import com.example.model.Booking;
import com.example.model.Package;
import com.example.model.Room;
import com.example.model.Offer;
import com.example.model.User;
import com.example.repository.BookingRepository;
import com.example.repository.PackageRepository;
import com.example.repository.RoomRepository;
import com.example.repository.OfferRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private PackageRepository packageRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private OfferRepository offerRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
    
    public Booking createBooking(Booking booking) {
        // Create a new booking entity to avoid concurrency issues
        Booking newBooking = new Booking();
        
        // Ensure we have managed entities by reloading them
        if (booking.getUser() != null) {
            User managedUser = userRepository.findById(booking.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            newBooking.setUser(managedUser);
        }
        
        if (booking.getRoom() != null) {
            Room managedRoom = roomRepository.findById(booking.getRoom().getId())
                    .orElse(null);
            newBooking.setRoom(managedRoom);
        }
        
        if (booking.getPackageEntity() != null) {
            Package managedPackage = packageRepository.findById(booking.getPackageEntity().getId())
                    .orElse(null);
            newBooking.setPackageEntity(managedPackage);
        }
        
        if (booking.getOffer() != null) {
            Offer managedOffer = offerRepository.findById(booking.getOffer().getId())
                    .orElse(null);
            newBooking.setOffer(managedOffer);
        }
        
        newBooking.setCheckInDate(booking.getCheckInDate());
        newBooking.setCheckOutDate(booking.getCheckOutDate());
        newBooking.setTotalAmount(booking.getTotalAmount());
        newBooking.setNumberOfGuests(booking.getNumberOfGuests());
        newBooking.setSpecialRequests(booking.getSpecialRequests());
        newBooking.setStatus(booking.getStatus());
        newBooking.setCreatedAt(java.time.LocalDateTime.now());
        newBooking.setUpdatedAt(java.time.LocalDateTime.now());
        
        return bookingRepository.save(newBooking);
    }
    
    public Booking updateBooking(Booking booking) {
        booking.setUpdatedAt(java.time.LocalDateTime.now());
        return bookingRepository.save(booking);
    }
    
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    
    public BigDecimal calculatePackagePrice(Package packageEntity, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return packageEntity.getPrice().multiply(BigDecimal.valueOf(nights));
    }
    
    public BigDecimal calculateRoomPrice(Room room, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return room.getPricePerNight().multiply(BigDecimal.valueOf(nights));
    }
    
    public BigDecimal calculateOfferPrice(Offer offer, BigDecimal basePrice) {
        if (offer.getDiscountPercentage() > 0) {
            BigDecimal discountAmount = basePrice.multiply(BigDecimal.valueOf(offer.getDiscountPercentage() / 100.0));
            return basePrice.subtract(discountAmount);
        }
        return basePrice;
    }
    
    public Optional<Package> getPackageById(Long id) {
        return packageRepository.findById(id);
    }
    
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }
    
    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public List<Booking> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByCheckInDateBetween(startDate, endDate);
    }
}
