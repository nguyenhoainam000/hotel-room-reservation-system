package org.example;

import java.time.LocalDate;
import java.util.List;

/**
 * Service facade providing hotel reservation and room management workflows.
 * Coordinates between Hotel inventory and HotelController.
 */
public class HotelReservationService {
    private final Hotel hotel;
    private final HotelController controller;

    public HotelReservationService() {
        this.hotel = new Hotel("Grand Azure Hotel", "123 Ocean Drive");
        this.controller = new HotelController(this.hotel);
    }

    public HotelReservationService(Hotel hotel) {
        this.hotel = hotel != null ? hotel : new Hotel("Grand Azure Hotel", "123 Ocean Drive");
        this.controller = new HotelController(this.hotel);
    }

    public Hotel getHotel() {
        return hotel;
    }

    public HotelController getController() {
        return controller;
    }

    public void addRoom(Room room) {
        hotel.addRoom(room);
    }

    public Room getRoom(String roomId) {
        return hotel.getRoom(roomId);
    }

    /**
     * User Story: View Room Availability
     * Checks if a specific room is available for booking.
     */
    public boolean viewRoomAvailability(String roomId) {
        return controller.viewRoomAvailability(roomId);
    }

    /**
     * UC-1: View Room Availability
     * Returns all rooms that are currently available.
     */
    public List<Room> viewAvailableRooms() {
        return controller.viewRoomAvailability();
    }

    /**
     * UC-2: Search rooms by type and location.
     * Pass null or blank for either criterion to match any value.
     */
    public List<Room> searchRooms(String type, String location) {
        return controller.searchRooms(type, location);
    }
  
    /**
     * User Story: Book Rooms for Guests
     * Reserves a room for a specific guest.
     * Throws IllegalArgumentException if guestName is blank.
     */
    public boolean bookRoom(String roomId, String guestName) {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty");
        }

        Room room = hotel.getRoom(roomId);
        if (room != null && room.isAvailable()) {
            Guest guest = new Guest(guestName, "N/A");
            LocalDate checkIn = LocalDate.now();
            LocalDate checkOut = checkIn.plusDays(1);
            controller.createReservation(guest, room, checkIn, checkOut);
            return true;
        }
        return false;
    }

    /**
     * Creates a formal reservation per Sequence Diagram UC-3.
     */
    public Reservation createReservation(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
        return controller.createReservation(guest, room, checkIn, checkOut);
    }

    /**
     * User Story: Cancel Reservation (UC-4)
     * Cancels an existing reservation by confirmation number and releases the room.
     */
    public boolean cancelReservation(String confirmationNumber) {
        return controller.cancelReservation(confirmationNumber);
    }

    /**
     * User Story: Room Preparation and Cleaning Alert
     * Triggers a cleaning alert when a guest checks out or room needs preparation.
     */
    public boolean triggerCleaningAlert(String roomId) {
        return controller.triggerCleaningAlert(roomId);
    }

    /**
     * Completes housekeeping and prepares the room for new guests.
     */
    public boolean completeCleaningAndPrepareRoom(String roomId) {
        return controller.completeCleaning(roomId);
    }

    /**
     * Updates room status directly.
     */
    public void updateRoomStatus(String roomId, RoomStatus status) {
        controller.updateRoomStatus(roomId, status);
    }

    /**
     * User Story: Generate Room Status Report (UC-6)
     * Produces a formatted status report for all rooms.
     */
    public String generateRoomStatusReport() {
        return controller.generateRoomStatusReport();
    }
}
