package org.example;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   HOTEL ROOM RESERVATION SYSTEM - DEMO RUN");
        System.out.println("==================================================");

        // 1. Initialize Hotel & Room Catalog
        Hotel hotel = new Hotel("Grand Azure Resort", "777 Coastal Blvd");
        RoomType standardType = new RoomType("Standard", "Queen", 120.0);
        RoomType deluxeType = new RoomType("Deluxe", "King", 200.0);
        RoomType suiteType = new RoomType("Executive Suite", "King", 350.0);

        hotel.addRoom(new Room("101", Location.STANDARD, RoomStatus.AVAILABLE, standardType));
        hotel.addRoom(new Room("102", Location.POOL_SIDE, RoomStatus.AVAILABLE, deluxeType));
        hotel.addRoom(new Room("201", Location.BALCONY, RoomStatus.AVAILABLE, suiteType));

        HotelController controller = new HotelController(hotel);

        // 2. Demonstration of UC-1: View Room Availability
        System.out.println("\n[UC-1] Viewing Available Rooms:");
        List<Room> availableRooms = controller.viewRoomAvailability();
        for (Room room : availableRooms) {
            System.out.printf("  - Room %s: %s | Bed: %s | Location: %s | Nightly: $%.2f%n",
                    room.getRoomNumber(),
                    room.getRoomType().getName(),
                    room.getRoomType().getBedSize(),
                    room.getLocation(),
                    room.getRoomType().getNightlyRate());
        }

        // 3. Demonstration of UC-3: Book Rooms for Guests (Create Reservation)
        System.out.println("\n[UC-3] Booking Room 102 for Guest 'Nguyen Hoai Nam' (3 Nights)...");
        Guest guest = new Guest("Nguyen Hoai Nam", "+1-555-0199");
        Room selectedRoom = hotel.getRoom("102");
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(3);

        Reservation reservation = controller.createReservation(guest, selectedRoom, checkIn, checkOut);
        System.out.printf("  Reservation Confirmed!%n");
        System.out.printf("  Confirmation Number: %s%n", reservation.getConfirmationNumber());
        System.out.printf("  Guest Name: %s (Phone: %s)%n", reservation.getGuest().getName(), reservation.getGuest().getPhone());
        System.out.printf("  Stay: %s to %s (3 nights)%n", reservation.getCheckInDate(), reservation.getCheckOutDate());
        System.out.printf("  Total Cost: $%.2f%n", reservation.getTotalCost());
        System.out.printf("  Room 102 Status Now: %s (isAvailable: %b)%n",
                selectedRoom.getStatus(), selectedRoom.isAvailable());

        // 4. Demonstration of UC-5: Room Preparation and Cleaning Alert
        System.out.println("\n[UC-5] Room Preparation & Cleaning Alert Workflow for Room 101:");
        System.out.println("  Initial Room 101 Status: " + hotel.getRoom("101").getStatus());

        System.out.println("  -> Triggering Cleaning Alert (Checkout / Housekeeping required)...");
        controller.triggerCleaningAlert("101");
        System.out.println("  Room 101 Status: " + hotel.getRoom("101").getStatus() +
                " (isAvailable: " + hotel.getRoom("101").isAvailable() + ")");

        System.out.println("  -> Housekeeping finishes cleaning & inspects room...");
        controller.completeCleaning("101");
        System.out.println("  Room 101 Status: " + hotel.getRoom("101").getStatus() +
                " (isAvailable: " + hotel.getRoom("101").isAvailable() + ")");

        System.out.println("\n==================================================");
        System.out.println("   DEMO COMPLETED SUCCESSFULLY");
        System.out.println("==================================================");
    }
}