package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelReservationService {
    private List<Room> rooms;

    public HotelReservationService() {
        this.rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    /**
     * User Story: View Room Availability
     * Checks if a specific room is available for booking.
     */
    public boolean viewRoomAvailability(String roomId) {
        Optional<Room> roomOpt = rooms.stream()
                .filter(r -> r.getRoomId().equalsIgnoreCase(roomId))
                .findFirst();

        return roomOpt.map(Room::isAvailable).orElse(false);
    }

    /**
     * User Story: Book Rooms for Guests
     * Reserves a room for a specific guest.
     */
    public boolean bookRoom(String roomId, String guestName) {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty");
        }

        Optional<Room> roomOpt = rooms.stream()
                .filter(r -> r.getRoomId().equalsIgnoreCase(roomId))
                .findFirst();

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            if (room.isAvailable()) {
                room.setAvailable(false);
                room.setGuestName(guestName);
                return true;
            }
        }
        return false;
    }

    /**
     * User Story: Room Preparation and Cleaning Alert
     * Triggers a cleaning alert when a guest checks out or room needs preparation.
     */
    public boolean triggerCleaningAlert(String roomId) {
        Optional<Room> roomOpt = rooms.stream()
                .filter(r -> r.getRoomId().equalsIgnoreCase(roomId))
                .findFirst();

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            room.setNeedsCleaning(true);
            return true;
        }
        return false;
    }

    /**
     * Completes housekeeping and prepares the room for new guests.
     */
    public boolean completeCleaningAndPrepareRoom(String roomId) {
        Optional<Room> roomOpt = rooms.stream()
                .filter(r -> r.getRoomId().equalsIgnoreCase(roomId))
                .findFirst();

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            room.setNeedsCleaning(false);
            room.setAvailable(true);
            room.setGuestName(null);
            return true;
        }
        return false;
    }
}
