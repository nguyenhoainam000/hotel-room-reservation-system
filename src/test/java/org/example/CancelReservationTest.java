package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CancelReservationTest {

    private Hotel hotel;
    private HotelController controller;

    @BeforeEach
    void setUp() {
        hotel = new Hotel("Ocean View Resort", "100 Beachway St");
        controller = new HotelController(hotel);

        RoomType standardType = new RoomType("Standard", "Queen", 100.0);

        hotel.addRoom(new Room("101", Location.STANDARD, RoomStatus.AVAILABLE, standardType));
        hotel.addRoom(new Room("102", Location.POOL_SIDE, RoomStatus.AVAILABLE, standardType));
    }

    /**
     * UC-4: Cancel Reservation
     */
    @Test
    @DisplayName("UC-4 Test 1: Cancelling a reservation frees the room and clears the guest")
    void testCancelReservationFreesRoom() {
        Room room = hotel.getRoom("101");
        Guest guest = new Guest("Alice Smith", "111-222-3333");
        Reservation reservation = controller.createReservation(
                guest, room, LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 12));
        assertEquals(RoomStatus.BOOKED, room.getStatus());

        boolean cancelled = controller.cancelReservation(reservation.getConfirmationNumber());

        assertTrue(cancelled);
        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
        assertNull(room.getGuestName());
        assertTrue(controller.viewRoomAvailability("101"));
    }

    @Test
    @DisplayName("UC-4 Test 2: Cancelling with an unknown or null confirmation number changes nothing")
    void testCancelUnknownReservationReturnsFalse() {
        Room room = hotel.getRoom("101");
        Guest guest = new Guest("Bob Jones", "444-555-6666");
        controller.createReservation(
                guest, room, LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 12));

        assertFalse(controller.cancelReservation("RES-UNKNOWN"));
        assertFalse(controller.cancelReservation(null));

        assertEquals(RoomStatus.BOOKED, room.getStatus());
        assertEquals("Bob Jones", room.getGuestName());
    }
}
