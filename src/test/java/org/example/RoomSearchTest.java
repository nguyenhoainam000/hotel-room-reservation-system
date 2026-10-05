package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomSearchTest {

    private Hotel hotel;
    private HotelController controller;

    @BeforeEach
    void setUp() {
        hotel = new Hotel("Ocean View Resort", "100 Beachway St");
        controller = new HotelController(hotel);

        RoomType standardType = new RoomType("Standard", "Queen", 100.0);
        RoomType deluxeType = new RoomType("Deluxe", "King", 200.0);

        hotel.addRoom(new Room("101", Location.STANDARD, RoomStatus.AVAILABLE, standardType));
        hotel.addRoom(new Room("102", Location.POOL_SIDE, RoomStatus.AVAILABLE, deluxeType));
        hotel.addRoom(new Room("103", Location.BALCONY, RoomStatus.AVAILABLE, deluxeType));
        hotel.addRoom(new Room("104", Location.POOL_SIDE, RoomStatus.AVAILABLE, standardType));
    }

    /**
     * UC-2: Search Rooms by Type and Location
     */
    @Test
    @DisplayName("UC-2 Test 1: Search filters rooms by type and by location")
    void testSearchFiltersByTypeAndLocation() {
        List<Room> deluxeRooms = controller.searchRooms("Deluxe", null);
        assertEquals(2, deluxeRooms.size());
        assertTrue(deluxeRooms.stream().allMatch(r -> r.getRoomType().getName().equals("Deluxe")));

        List<Room> poolSideRooms = controller.searchRooms(null, "POOL_SIDE");
        assertEquals(2, poolSideRooms.size());
        assertTrue(poolSideRooms.stream().allMatch(r -> r.getLocation() == Location.POOL_SIDE));

        List<Room> deluxePoolSide = controller.searchRooms("deluxe", "pool_side");
        assertEquals(1, deluxePoolSide.size());
        assertEquals("102", deluxePoolSide.get(0).getRoomNumber());
    }

    @Test
    @DisplayName("UC-2 Test 2: Search returns only available rooms and empty list when nothing matches")
    void testSearchExcludesUnavailableRooms() {
        controller.updateRoomStatus("102", RoomStatus.BOOKED);
        controller.triggerCleaningAlert("103");

        assertTrue(controller.searchRooms("Deluxe", null).isEmpty());

        List<Room> poolSideRooms = controller.searchRooms(null, "POOL_SIDE");
        assertEquals(1, poolSideRooms.size());
        assertEquals("104", poolSideRooms.get(0).getRoomNumber());
    }
}
