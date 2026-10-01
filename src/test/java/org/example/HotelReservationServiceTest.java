package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HotelReservationServiceTest {

    private HotelReservationService service;

    @BeforeEach
    void setUp() {
        service = new HotelReservationService();
        service.addRoom(new Room("101", "Deluxe"));
        service.addRoom(new Room("102", "Standard"));
    }

    /**
     * Unit Test 1: Tests "Book Rooms for Guests" and "View Room Availability"
     */
    @Test
    @DisplayName("Test 1: Book room successfully and verify room availability updates")
    void testBookRoomSuccessfullyAndCheckAvailability() {
        // Before booking: Room 101 must be available
        assertTrue(service.viewRoomAvailability("101"), "Room 101 should initially be available");

        // Book room for guest John Doe
        boolean bookingResult = service.bookRoom("101", "John Doe");
        assertTrue(bookingResult, "Booking room 101 should be successful");

        // After booking: Room 101 must no longer be available
        assertFalse(service.viewRoomAvailability("101"), "Room 101 should be unavailable after booking");

        // Attempting to book an already occupied room should return false
        boolean secondBookingResult = service.bookRoom("101", "Jane Smith");
        assertFalse(secondBookingResult, "Cannot book an already occupied room");
    }

    /**
     * Unit Test 2: Tests "Room Preparation and Cleaning Alert"
     */
    @Test
    @DisplayName("Test 2: Trigger cleaning alert and verify room preparation process")
    void testRoomPreparationAndCleaningAlert() {
        // Room 102 is initially ready for guests
        assertTrue(service.viewRoomAvailability("102"), "Room 102 should initially be available");

        // Trigger cleaning alert (e.g. after a guest checks out)
        boolean alertTriggered = service.triggerCleaningAlert("102");
        assertTrue(alertTriggered, "Should trigger cleaning alert successfully");

        // While cleaning: room must not be available for new bookings
        assertFalse(service.viewRoomAvailability("102"), "Room needing cleaning should not be available for booking");

        // Housekeeping completes cleaning and prepares the room
        boolean cleaningCompleted = service.completeCleaningAndPrepareRoom("102");
        assertTrue(cleaningCompleted, "Cleaning and room preparation completed successfully");

        // Room 102 should now be available again
        assertTrue(service.viewRoomAvailability("102"), "Room 102 should be available after cleaning is complete");
    }

    /**
     * Unit Test 3: Tests validation when guest name is blank
     */
    @Test
    @DisplayName("Test 3: Throw exception when guest name is empty")
    void testBookRoomWithEmptyGuestNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.bookRoom("101", "");
        }, "Should throw IllegalArgumentException when guest name is empty");
    }
}
