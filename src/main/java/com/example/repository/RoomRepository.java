package com.example.repository;

import com.example.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    List<Room> findByActiveTrueOrderByRoomNumber();
    
    List<Room> findByRoomTypeAndActiveTrue(String roomType);
    
    List<Room> findByStatusAndActiveTrue(Room.RoomStatus status);
    
    List<Room> findByCapacityGreaterThanEqualAndActiveTrue(Integer capacity);
    
    boolean existsByRoomNumber(String roomNumber);
}

