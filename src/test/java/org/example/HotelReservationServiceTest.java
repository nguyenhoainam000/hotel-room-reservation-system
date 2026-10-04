package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HotelReservationServiceTest {

    private Hotel hotel;
    private HotelController controller;
    private HotelReservationService service;
    private RoomType deluxeType;
    private RoomType standardType;

    @BeforeEach
    void setUp() {
        hotel = new Hotel("Ocean View Resort", "100 Beachway St");
        controller = new HotelController(hotel);
        service = new HotelReservationService(hotel);

        standardType = new RoomType("Standard", "Queen", 100.0);
        deluxeType = new RoomType("Deluxe", "King", 200.0);

        hotel.addRoom(new Room("101", Location.STANDARD, RoomStatus.AVAILABLE, standardType));
        hotel.addRoom(new Room("102", Location.POOL_SIDE, RoomStatus.AVAILABLE, deluxeType));
        hotel.addRoom(new Room("103", Location.BALCONY, RoomStatus.AVAILABLE, deluxeType));
    }

    /**
     * UC-1: View Room Availability
     */
    @Test
    @DisplayName("UC-1 Test 1: Verify available rooms retrieval")
    void testViewRoomAvailabilityReturnsAvailableRooms() {
        List<Room> availableRooms = controller.viewRoomAvailability();
        assertEquals(3, availableRooms.size(), "All 3 initial rooms should be available");

        // Mark Room 101 as BOOKED
        hotel.getRoom("101").updateStatus(RoomStatus.BOOKED);

        availableRooms = controller.viewRoomAvailability();
        assertEquals(2, availableRooms.size(), "Only 2 rooms should be available after Room 101 is booked");
        assertFalse(controller.viewRoomAvailability("101"), "Room 101 should report unavailable");
        assertTrue(controller.viewRoomAvailability("102"), "Room 102 should report available");
    }

    @Test
    @DisplayName("UC-1 Test 2: Room isReadyForGuest and isAvailable state transitions")
    void testRoomAvailabilityStates() {
        Room room = hotel.getRoom("101");
        assertTrue(room.isAvailable(), "Room should be available initially");
        assertTrue(room.isReadyForGuest(), "Room should be ready for guest initially");

        room.updateStatus(RoomStatus.CLEANING);
        assertFalse(room.isAvailable(), "Room under cleaning must not be available");
        assertFalse(room.isReadyForGuest(), "Room under cleaning must not be ready for guest");

        room.updateStatus(RoomStatus.OUT_OF_SERVICE);
        assertFalse(room.isAvailable(), "Out of service room must not be available");
        assertFalse(room.isReadyForGuest(), "Out of service room must not be ready for guest");

        room.updateStatus(RoomStatus.AVAILABLE);
        assertTrue(room.isAvailable(), "Available room must be available");
        assertTrue(room.isReadyForGuest(), "Available room must be ready for guest");
    }

    /**
     * UC-3: Book Rooms for Guests (Create Reservation)
     */
    @Test
    @DisplayName("UC-3 Test 1: Create reservation successfully with confirmation and cost calculation")
    void testCreateReservationSuccess() {
        Guest guest = new Guest("Nguyen Hoai Nam", "+1-555-0144");
        Room room = hotel.getRoom("102"); // Deluxe, 200/night
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(3);

        Reservation res = controller.createReservation(guest, room, checkIn, checkOut);

        assertNotNull(res, "Reservation must be created");
        assertNotNull(res.getConfirmationNumber(), "Confirmation number must be assigned");
        assertTrue(res.getConfirmationNumber().startsWith("RES-"), "Confirmation number should start with RES-");
        assertEquals("Nguyen Hoai Nam", res.getGuest().getName());
        assertEquals("102", res.getRoom().getRoomNumber());
        assertEquals(600.0, res.getTotalCost(), 0.001, "Total cost should be 3 nights * $200 = $600");
        assertEquals(RoomStatus.BOOKED, room.getStatus(), "Room status must transition to BOOKED");
        assertFalse(room.isAvailable(), "Room must not be available after booking");
        assertEquals(1, controller.getReservations().size(), "Reservation must be recorded in system");
    }

    @Test
    @DisplayName("UC-3 Test 2: Reject booking for already occupied or booked room")
    void testBookingAlreadyOccupiedRoomThrowsException() {
        Guest guest1 = new Guest("Alice Smith", "111-222-3333");
        Guest guest2 = new Guest("Bob Jones", "444-555-6666");
        Room room = hotel.getRoom("101");
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(2);

        controller.createReservation(guest1, room, checkIn, checkOut);

        // Attempting to book the same room should fail
        assertThrows(IllegalStateException.class, () -> {
            controller.createReservation(guest2, room, checkIn, checkOut);
        }, "Should throw IllegalStateException when booking an unavailable room");
    }

    @Test
    @DisplayName("UC-3 Alternate Flow 5a: Reject reservation when guest name is blank")
    void testReservationWithBlankGuestNameRejected() {
        Room room = hotel.getRoom("103");
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(1);

        assertThrows(IllegalArgumentException.class, () -> {
            new Guest("", "123-456");
        }, "Should throw IllegalArgumentException when creating guest with blank name");

        assertThrows(IllegalArgumentException.class, () -> {
            controller.createReservation(new Guest("   ", "123"), room, checkIn, checkOut);
        }, "Should throw IllegalArgumentException when guest name is whitespace");

        assertThrows(IllegalArgumentException.class, () -> {
            service.bookRoom("103", "");
        }, "Facade service should also reject blank guest name");
    }

    @Test
    @DisplayName("UC-3 Test 4: Reject reservation with invalid dates")
    void testReservationWithInvalidDatesRejected() {
        Guest guest = new Guest("Charlie Brown", "555-777-8888");
        Room room = hotel.getRoom("103");
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.minusDays(1); // Check-out before check-in

        assertThrows(IllegalArgumentException.class, () -> {
            controller.createReservation(guest, room, checkIn, checkOut);
        }, "Should reject check-out date prior to check-in date");
    }

    /**
     * UC-5: Room Preparation and Cleaning Alert (Update Room Status)
     */
    @Test
    @DisplayName("UC-5 Test 1: Trigger cleaning alert and complete room preparation lifecycle")
    void testCleaningAlertAndPreparationLifecycle() {
        Room room = hotel.getRoom("102");
        assertTrue(room.isAvailable(), "Room 102 should be initially available");

        // 1. Housekeeping / System triggers cleaning alert
        boolean alertTriggered = controller.triggerCleaningAlert("102");
        assertTrue(alertTriggered, "Cleaning alert should be successfully triggered");
        assertEquals(RoomStatus.CLEANING, room.getStatus(), "Room status should be CLEANING");
        assertFalse(room.isAvailable(), "Room under CLEANING status must not be available for guests");

        // 2. Housekeeping finishes cleaning & room is inspected
        boolean cleaningFinished = controller.completeCleaning("102");
        assertTrue(cleaningFinished, "Cleaning completion should succeed");
        assertEquals(RoomStatus.AVAILABLE, room.getStatus(), "Room status should return to AVAILABLE");
        assertTrue(room.isAvailable(), "Room should be available again after cleaning");
    }

    @Test
    @DisplayName("UC-5 Test 2: Update room status directly for staff")
    void testUpdateRoomStatusDirectly() {
        controller.updateRoomStatus("103", RoomStatus.OUT_OF_SERVICE);
        assertEquals(RoomStatus.OUT_OF_SERVICE, hotel.getRoom("103").getStatus());
        assertFalse(hotel.getRoom("103").isAvailable());

        controller.updateRoomStatus("103", RoomStatus.AVAILABLE);
        assertEquals(RoomStatus.AVAILABLE, hotel.getRoom("103").getStatus());
        assertTrue(hotel.getRoom("103").isAvailable());
    }

    /**
     * Facade service backward compatibility and helper tests
     */
    @Test
    @DisplayName("Facade Service: bookRoom and cleaning helpers integration")
    void testFacadeServiceIntegration() {
        assertTrue(service.viewRoomAvailability("101"));
        assertTrue(service.bookRoom("101", "John Doe"));
        assertFalse(service.viewRoomAvailability("101"));

        // Cleaning alert through service
        assertTrue(service.triggerCleaningAlert("101"));
        assertEquals(RoomStatus.CLEANING, hotel.getRoom("101").getStatus());

        // Complete cleaning through service
        assertTrue(service.completeCleaningAndPrepareRoom("101"));
        assertEquals(RoomStatus.AVAILABLE, hotel.getRoom("101").getStatus());
        assertTrue(service.viewRoomAvailability("101"));
    }

    /**
     * UC-4: Cancel Room Reservations
     */
    void testCancelReservationByConfirmationNumber() {
        Guest guest = new Guest("Test Guest", "555-111-2222");
        Room room = hotel.getRoom("101");

        Reservation reservation = controller.createReservation(
                guest,
                room,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        assertEquals(RoomStatus.BOOKED, room.getStatus());

        boolean cancelled =
                controller.cancelReservation(reservation.getConfirmationNumber());

        assertTrue(cancelled);
        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
        assertNull(room.getGuestName());
    }
    /**
     * UC-4: Cancel Room Reservations
     */
    @Test
    @DisplayName("UC-4 Test 1: Cancel reservation by confirmation number")
    void testCancelReservationByConfirmationNumber1() {
        Guest guest = new Guest("Test Guest", "555-111-2222");
        Room room = hotel.getRoom("101");

        Reservation reservation = controller.createReservation(
                guest,
                room,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        assertEquals(RoomStatus.BOOKED, room.getStatus());

        boolean cancelled =
                controller.cancelReservation(reservation.getConfirmationNumber());

        assertTrue(cancelled);
        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
        assertNull(room.getGuestName());
    }

    @Test
    @DisplayName("UC-4 Test 2: Cancel reservation by room number")
    void testCancelReservationByRoomNumber() {
        Guest guest = new Guest("Test Guest", "555-111-2222");
        Room room = hotel.getRoom("102");

        controller.createReservation(
                guest,
                room,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        assertEquals(RoomStatus.BOOKED, room.getStatus());

        boolean cancelled = controller.cancelReservationByRoom("102");

        assertTrue(cancelled);
        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
        assertNull(room.getGuestName());

        assertFalse(controller.cancelReservationByRoom("999"));
    }/**
     * UC-6: Generate Room Status Reports
     */
    @Test
    @DisplayName("UC-6 Test 1: Generate accurate room status totals")
    void testGenerateRoomStatusReportCounts() {
        hotel.getRoom("101").updateStatus(RoomStatus.BOOKED);
        hotel.getRoom("102").updateStatus(RoomStatus.CLEANING);
        hotel.getRoom("103").updateStatus(RoomStatus.AVAILABLE);

        String report = controller.generateRoomStatusReport();

        assertTrue(report.contains("Total Rooms: 3"));
        assertTrue(report.contains("Available Rooms: 1"));
        assertTrue(report.contains("Booked Rooms: 1"));
        assertTrue(report.contains("Cleaning Rooms: 1"));
        assertTrue(report.contains("Out of Service Rooms: 0"));
    }

    @Test
    @DisplayName("UC-6 Test 2: Calculate occupancy rate and format report")
    void testGenerateRoomStatusReportOccupancyRate() {
        hotel.getRoom("101").updateStatus(RoomStatus.BOOKED);
        hotel.getRoom("102").updateStatus(RoomStatus.AVAILABLE);
        hotel.getRoom("103").updateStatus(RoomStatus.AVAILABLE);

        String report = controller.generateRoomStatusReport();

        assertTrue(report.contains("=== Room Status Report ==="));
        assertTrue(report.contains("Occupancy Rate: 33.33%"));
        assertTrue(report.contains("Room 101"));
        assertTrue(report.contains("Room 102"));
        assertTrue(report.contains("Room 103"));
    }
}
