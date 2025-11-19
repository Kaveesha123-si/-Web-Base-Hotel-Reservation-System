package com.example.service;

import com.example.model.Room;
import com.example.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    public List<Room> getActiveRooms() {
        return roomRepository.findByActiveTrueOrderByRoomNumber();
    }
    
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }
    
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }
    
    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }
    
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
    
    public List<Room> getRoomsByType(String roomType) {
        return roomRepository.findByRoomTypeAndActiveTrue(roomType);
    }
    
    public List<Room> getAvailableRooms() {
        return roomRepository.findByStatusAndActiveTrue(Room.RoomStatus.AVAILABLE);
    }
    
    public List<Room> getRoomsByCapacity(Integer capacity) {
        return roomRepository.findByCapacityGreaterThanEqualAndActiveTrue(capacity);
    }
    
    public boolean isRoomNumberExists(String roomNumber) {
        return roomRepository.existsByRoomNumber(roomNumber);
    }
    
    public boolean isRoomNumberExistsForUpdate(String roomNumber, Long roomId) {
        return roomRepository.existsByRoomNumber(roomNumber) && 
               !roomRepository.findById(roomId).map(room -> room.getRoomNumber().equals(roomNumber)).orElse(false);
    }
}

