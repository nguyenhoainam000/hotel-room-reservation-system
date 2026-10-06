package org.example;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Main application console interface providing an interactive task menu.
 * Enables the Hotel Manager and staff to execute all core use cases (UC-1 through UC-6).
 */
public class Main {

    private static final Hotel hotel = new Hotel("Grand Azure Resort", "777 Coastal Blvd");
    private static final HotelReservationService service = new HotelReservationService(hotel);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSampleData();

        System.out.println("======================================================================");
        System.out.println("        WELCOME TO HOTEL ROOM RESERVATION SYSTEM (MVP)");
        System.out.println("             Hotel: " + hotel.getName());
        System.out.println("======================================================================");

        boolean running = true;
        while (running) {
            displayMenu();
            System.out.print("Enter your choice (0-8): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> handleViewAvailableRooms();
                case "2" -> handleSearchRooms();
                case "3" -> handleBookRoom();
                case "4" -> handleCancelReservation();
                case "5" -> handleTriggerCleaningAlert();
                case "6" -> handleCompleteCleaning();
                case "7" -> handleGenerateReport();
                case "8" -> handleRunAutomatedDemo();
                case "0" -> {
                    System.out.println("\nThank you for using Hotel Reservation System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("\n[!] Invalid option. Please enter a number between 0 and 8.");
            }
        }
    }

    /**
     * Populates initial hotel room inventory.
     */
    private static void initializeSampleData() {
        RoomType standardType = new RoomType("Standard", "Queen", 120.0);
        RoomType doubleType = new RoomType("Standard Double", "Double", 140.0);
        RoomType deluxeType = new RoomType("Deluxe", "King", 200.0);
        RoomType suiteType = new RoomType("Executive Suite", "King", 350.0);

        hotel.addRoom(new Room("101", Location.STANDARD, RoomStatus.AVAILABLE, standardType));
        hotel.addRoom(new Room("102", Location.POOL_SIDE, RoomStatus.AVAILABLE, deluxeType));
        hotel.addRoom(new Room("103", Location.STANDARD, RoomStatus.AVAILABLE, doubleType));
        hotel.addRoom(new Room("201", Location.BALCONY, RoomStatus.AVAILABLE, suiteType));
        hotel.addRoom(new Room("202", Location.POOL_SIDE, RoomStatus.AVAILABLE, deluxeType));
        hotel.addRoom(new Room("203", Location.BALCONY, RoomStatus.AVAILABLE, standardType));
    }

    /**
     * Displays main task navigation menu.
     */
    private static void displayMenu() {
        System.out.println("\n======================================================================");
        System.out.println("                          TASK ACTION MENU");
        System.out.println("======================================================================");
        System.out.println("  1. View All Available Rooms                  [UC-1]");
        System.out.println("  2. Search Rooms by Type & Location           [UC-2]");
        System.out.println("  3. Book a Room for Guest                     [UC-3]");
        System.out.println("  4. Cancel a Reservation                      [UC-4]");
        System.out.println("  5. Housekeeping: Trigger Cleaning Alert      [UC-5]");
        System.out.println("  6. Housekeeping: Mark Cleaning Complete      [UC-5]");
        System.out.println("  7. Generate Room Status Report               [UC-6]");
        System.out.println("  8. Run Automated Demonstration Scenario");
        System.out.println("  0. Exit Application");
        System.out.println("======================================================================");
    }

    /**
     * UC-1: View All Available Rooms
     */
    private static void handleViewAvailableRooms() {
        System.out.println("\n--- [UC-1] AVAILABLE HOTEL ROOMS ---");
        List<Room> availableRooms = service.viewAvailableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("No rooms are currently available.");
            return;
        }

        printRoomTable(availableRooms);
        System.out.println("Total Available: " + availableRooms.size() + " room(s)");
    }

    /**
     * UC-2: Search Rooms by Type & Location
     */
    private static void handleSearchRooms() {
        System.out.println("\n--- [UC-2] SEARCH ROOMS BY CRITERIA ---");
        System.out.print("Enter desired room type (e.g. Standard, Deluxe, Suite, or press Enter for any): ");
        String type = scanner.nextLine().trim();

        System.out.print("Enter desired location (STANDARD, POOL_SIDE, BALCONY, or press Enter for any): ");
        String location = scanner.nextLine().trim();

        List<Room> results = service.searchRooms(type, location);
        System.out.println("\n--- Search Results ---");
        if (results.isEmpty()) {
            System.out.println("No rooms found matching the specified criteria.");
        } else {
            printRoomTable(results);
            System.out.println("Matching Rooms Found: " + results.size());
        }
    }

    /**
     * UC-3: Book a Room for Guest
     */
    private static void handleBookRoom() {
        System.out.println("\n--- [UC-3] BOOK A ROOM FOR GUEST ---");
        List<Room> availableRooms = service.viewAvailableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for booking right now.");
            return;
        }

        System.out.println("Currently Available Rooms:");
        printRoomTable(availableRooms);

        System.out.print("\nEnter Room Number to book: ");
        String roomNumber = scanner.nextLine().trim();
        Room room = hotel.getRoom(roomNumber);

        if (room == null) {
            System.out.println("[!] Error: Room '" + roomNumber + "' does not exist.");
            return;
        }
        if (!room.isAvailable()) {
            System.out.println("[!] Error: Room '" + roomNumber + "' is not available (Status: " + room.getStatus() + ").");
            return;
        }

        System.out.print("Enter Guest Full Name: ");
        String guestName = scanner.nextLine().trim();

        // Enforce Sequence Diagram Alternate Flow 5a
        if (guestName.isEmpty()) {
            System.out.println("[!] Error (Alternate Flow 5a): Guest name cannot be blank. Booking rejected.");
            return;
        }

        System.out.print("Enter Guest Phone Number: ");
        String guestPhone = scanner.nextLine().trim();
        if (guestPhone.isEmpty()) {
            guestPhone = "N/A";
        }

        System.out.print("Enter Number of Nights to Stay (default 1): ");
        String nightsStr = scanner.nextLine().trim();
        int nights = 1;
        if (!nightsStr.isEmpty()) {
            try {
                nights = Integer.parseInt(nightsStr);
                if (nights <= 0) {
                    System.out.println("[!] Stay duration must be at least 1 night. Defaulting to 1.");
                    nights = 1;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid number format. Defaulting to 1 night.");
                nights = 1;
            }
        }

        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(nights);
        Guest guest = new Guest(guestName, guestPhone);

        try {
            Reservation reservation = service.createReservation(guest, room, checkIn, checkOut);
            System.out.println("\n==================================================");
            System.out.println("           RESERVATION CONFIRMED SUCCESSFULLY");
            System.out.println("==================================================");
            System.out.println("  Confirmation Code : " + reservation.getConfirmationNumber());
            System.out.println("  Guest Name        : " + reservation.getGuest().getName());
            System.out.println("  Contact Phone     : " + reservation.getGuest().getPhone());
            System.out.println("  Room Reserved     : " + reservation.getRoom().getRoomNumber() +
                    " (" + reservation.getRoom().getRoomType().getName() + " - " + reservation.getRoom().getLocation() + ")");
            System.out.println("  Stay Dates        : " + reservation.getCheckInDate() + " to " + reservation.getCheckOutDate() +
                    " (" + nights + " night" + (nights > 1 ? "s" : "") + ")");
            System.out.printf("  Total Stay Cost   : $%.2f%n", reservation.getTotalCost());
            System.out.println("  Updated Room State: " + room.getStatus() + " (isAvailable: " + room.isAvailable() + ")");
            System.out.println("==================================================");
        } catch (Exception e) {
            System.out.println("[!] Booking Failed: " + e.getMessage());
        }
    }

    /**
     * UC-4: Cancel a Reservation
     */
    private static void handleCancelReservation() {
        System.out.println("\n--- [UC-4] CANCEL RESERVATION ---");
        System.out.print("Enter Confirmation Code to cancel (e.g. RES-XXXXXXXX): ");
        String code = scanner.nextLine().trim();

        if (code.isEmpty()) {
            System.out.println("[!] Confirmation code cannot be empty.");
            return;
        }

        boolean success = service.cancelReservation(code);
        if (success) {
            System.out.println("[✓] Success: Reservation '" + code + "' has been cancelled.");
            System.out.println("    The room has been released and is now AVAILABLE in inventory.");
        } else {
            System.out.println("[!] Error: No active reservation found with confirmation code: " + code);
        }
    }

    /**
     * UC-5: Trigger Cleaning Alert
     */
    private static void handleTriggerCleaningAlert() {
        System.out.println("\n--- [UC-5] TRIGGER HOUSEKEEPING CLEANING ALERT ---");
        System.out.print("Enter Room Number that requires cleaning: ");
        String roomNumber = scanner.nextLine().trim();

        Room room = hotel.getRoom(roomNumber);
        if (room == null) {
            System.out.println("[!] Error: Room '" + roomNumber + "' does not exist.");
            return;
        }

        boolean alertTriggered = service.triggerCleaningAlert(roomNumber);
        if (alertTriggered) {
            System.out.println("[✓] Cleaning Alert Triggered!");
            System.out.println("    Room " + roomNumber + " status is now " + room.getStatus() +
                    " (isAvailable: " + room.isAvailable() + "). Room is locked from bookings.");
        }
    }

    /**
     * UC-5: Complete Cleaning and Prepare Room
     */
    private static void handleCompleteCleaning() {
        System.out.println("\n--- [UC-5] COMPLETE HOUSEKEEPING & PREPARATION ---");
        System.out.print("Enter Room Number that has been cleaned and inspected: ");
        String roomNumber = scanner.nextLine().trim();

        Room room = hotel.getRoom(roomNumber);
        if (room == null) {
            System.out.println("[!] Error: Room '" + roomNumber + "' does not exist.");
            return;
        }

        boolean complete = service.completeCleaningAndPrepareRoom(roomNumber);
        if (complete) {
            System.out.println("[✓] Housekeeping Complete!");
            System.out.println("    Room " + roomNumber + " status is now " + room.getStatus() +
                    " (isAvailable: " + room.isAvailable() + "). Room is ready for incoming guests.");
        }
    }

    /**
     * UC-6: Generate Room Status Report
     */
    private static void handleGenerateReport() {
        System.out.println("\n--- [UC-6] GENERATING OCCUPANCY & ROOM STATUS REPORT ---");
        String report = service.generateRoomStatusReport();
        System.out.println(report);
    }

    /**
     * Automated Scenario Walkthrough
     */
    private static void handleRunAutomatedDemo() {
        System.out.println("\n>>> EXECUTING AUTOMATED DEMONSTRATION WALKTHROUGH <<<");

        System.out.println("\n1. [UC-1] Available Rooms:");
        List<Room> rooms = service.viewAvailableRooms();
        printRoomTable(rooms);

        System.out.println("\n2. [UC-3] Booking Room 102 for 'Alice Johnson' (2 Nights)...");
        Room room102 = hotel.getRoom("102");
        Reservation res = service.createReservation(
                new Guest("Alice Johnson", "+1-555-0144"),
                room102,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );
        System.out.println("   Confirmed! Code: " + res.getConfirmationNumber() + " | Total Cost: $" + res.getTotalCost());
        System.out.println("   Room 102 Status: " + room102.getStatus() + " (isAvailable: " + room102.isAvailable() + ")");

        System.out.println("\n3. [UC-5] Room 101 Checkout: Triggering Cleaning Alert...");
        service.triggerCleaningAlert("101");
        System.out.println("   Room 101 Status: " + hotel.getRoom("101").getStatus() + " (Locked from booking)");

        System.out.println("\n4. [UC-5] Housekeeping Finishes Cleaning Room 101...");
        service.completeCleaningAndPrepareRoom("101");
        System.out.println("   Room 101 Status: " + hotel.getRoom("101").getStatus() + " (Ready for guests)");

        System.out.println("\n5. [UC-4] Cancelling Alice's Reservation (" + res.getConfirmationNumber() + ")...");
        service.cancelReservation(res.getConfirmationNumber());
        System.out.println("   Room 102 Status After Cancellation: " + room102.getStatus() + " (Released back to inventory)");

        System.out.println("\n>>> DEMONSTRATION WALKTHROUGH COMPLETED <<<\n");
    }

    /**
     * Helper method to print formatted room table.
     */
    private static void printRoomTable(List<Room> roomList) {
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("%-8s %-18s %-12s %-14s %-12s %-12s%n",
                "Room", "Type", "Bed Size", "Location", "Rate/Night", "Status");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Room r : roomList) {
            String typeName = r.getRoomType() != null ? r.getRoomType().getName() : "Standard";
            String bedSize = r.getRoomType() != null ? r.getRoomType().getBedSize() : "Queen";
            double rate = r.getRoomType() != null ? r.getRoomType().getNightlyRate() : 100.0;
            System.out.printf("%-8s %-18s %-12s %-14s $%-11.2f %-12s%n",
                    r.getRoomNumber(), typeName, bedSize, r.getLocation(), rate, r.getStatus());
        }
        System.out.println("-----------------------------------------------------------------------------------------");
    }
}