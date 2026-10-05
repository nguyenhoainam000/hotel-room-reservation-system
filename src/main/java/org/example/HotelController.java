package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller handling hotel operations and coordination between UI and domain models.
 * As defined in full_class_diagram.txt.
 * Implements UC-1, UC-3, and UC-5 assigned features.
 */
public class HotelController {
    private final Hotel hotel;
    private final List<Reservation> reservations;

    public HotelController(Hotel hotel) {
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel cannot be null");
        }
        this.hotel = hotel;
        this.reservations = new ArrayList<>();
    }

    public Hotel getHotel() {
        return hotel;
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    /**
     * UC-1: View Room Availability
     * Returns all rooms that are currently AVAILABLE.
     */
    public List<Room> viewRoomAvailability() {
        return hotel.getRooms().stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * Checks availability of a specific room by room number.
     */
    public boolean viewRoomAvailability(String roomNumber) {
        Room room = hotel.getRoom(roomNumber);
        return room != null && room.isAvailable();
    }

    /**
     * UC-2: Search Rooms by Type and Location
     */
    public List<Room> searchRooms(String type, String location) {
        return hotel.getRooms().stream()
                .filter(Room::isAvailable)
                .filter(r -> type == null || (r.getRoomType() != null && r.getRoomType().getName().equalsIgnoreCase(type)))
                .filter(r -> location == null || r.getLocation().name().equalsIgnoreCase(location))
                .collect(Collectors.toList());
    }

    /**
     * UC-3: Create Reservation (Book Rooms for Guests)
     * Follows the sequence diagram flow:
     * - Validates guest details (Alternate Flow 5a: reject if guest name is blank)
     * - Verifies room is available
     * - Assigns unique confirmation number & marks room "Booked"
     * - Saves reservation and returns confirmation details
     */
    public Reservation createReservation(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
        if (guest == null || guest.getName() == null || guest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        if (!room.isAvailable()) {
            throw new IllegalStateException("Room " + room.getRoomNumber() + " is not available for booking");
        }
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Invalid check-in/check-out dates");
        }

        // Generate confirmation number
        String confirmationNumber = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Mark room as BOOKED
        room.updateStatus(RoomStatus.BOOKED);
        room.setGuestName(guest.getName());

        Reservation reservation = new Reservation(confirmationNumber, guest, room, checkIn, checkOut);
        reservations.add(reservation);
        return reservation;
    }

    /**
     * UC-4: Cancel Reservation
     */
    public boolean cancelReservation(String confirmationNumber) {
        if (confirmationNumber == null) return false;
        for (Reservation res : reservations) {
            if (res.getConfirmationNumber().equalsIgnoreCase(confirmationNumber)) {
                res.cancelReservation();
                return true;
            }
        }
        return false;
    }
    /**
     * UC-4: Cancel Reservation by room number.
     * Locates an existing reservation using the room ID
     * and releases the room back to available inventory.
     */
    public boolean cancelReservationByRoom(String roomNumber) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            return false;
        }

        for (Reservation res : reservations) {
            if (res.getRoom() != null &&
                    res.getRoom().getRoomNumber().equalsIgnoreCase(roomNumber)) {

                res.cancelReservation();
                return true;
            }
        }

        return false;
    }
    /**
     * UC-5: Update Room Status (Room Preparation and Cleaning Alert)
     * Allows Housekeeping or Front Desk Clerk to update room status.
     */
    public void updateRoomStatus(String roomNumber, RoomStatus status) {
        Room room = hotel.getRoom(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room not found: " + roomNumber);
        }
        room.updateStatus(status);
        if (status == RoomStatus.AVAILABLE) {
            room.setGuestName(null);
        }
    }

    /**
     * UC-5 Helper: Triggers cleaning alert for room preparation.
     */
    public boolean triggerCleaningAlert(String roomNumber) {
        Room room = hotel.getRoom(roomNumber);
        if (room != null) {
            room.updateStatus(RoomStatus.CLEANING);
            return true;
        }
        return false;
    }

    /**
     * UC-5 Helper: Completes cleaning and prepares room for new guests.
     */
    public boolean completeCleaning(String roomNumber) {
        Room room = hotel.getRoom(roomNumber);
        if (room != null) {
            room.updateStatus(RoomStatus.AVAILABLE);
            room.setGuestName(null);
            return true;
        }
        return false;
    }

public String generateRoomStatusReport() {
    	    int totalRooms = hotel.getRooms().size();
    	    int availableRooms = 0;
    	    int bookedRooms = 0;
    	    int cleaningRooms = 0;
    	    int outOfServiceRooms = 0;

    	    for (Room room : hotel.getRooms()) {
    	        switch (room.getStatus()) {
    	            case AVAILABLE:
    	                availableRooms++;
    	                break;

    	            case BOOKED:
    	                bookedRooms++;
    	                break;

    	            case CLEANING:
    	                cleaningRooms++;
    	                break;

    	            case OUT_OF_SERVICE:
    	                outOfServiceRooms++;
    	                break;
    	        }
    	    }

    	    double occupancyRate =
    	            totalRooms == 0
    	                    ? 0.0
    	                    : ((double) bookedRooms / totalRooms) * 100.0;

    	    StringBuilder sb = new StringBuilder();

    	    sb.append("=== Room Status Report ===\n");
    	    sb.append("Total Rooms: ").append(totalRooms).append("\n");
    	    sb.append("Available Rooms: ").append(availableRooms).append("\n");
    	    sb.append("Booked Rooms: ").append(bookedRooms).append("\n");
    	    sb.append("Cleaning Rooms: ").append(cleaningRooms).append("\n");
    	    sb.append("Out of Service Rooms: ")
    	            .append(outOfServiceRooms).append("\n");

    	    sb.append(String.format(
    	            "Occupancy Rate: %.2f%%%n",
    	            occupancyRate
    	    ));

    	    sb.append("\nRoom Details:\n");

    	    for (Room room : hotel.getRooms()) {
    	        sb.append(String.format(
    	                "Room %s [%s] - Status: %s - Type: %s%n",
    	                room.getRoomNumber(),
    	                room.getLocation(),
    	                room.getStatus(),
    	                room.getRoomType() != null
    	                        ? room.getRoomType().getName()
    	                        : "N/A"
    	        ));
    	    }

    	    return sb.toString();
    	}
    }
