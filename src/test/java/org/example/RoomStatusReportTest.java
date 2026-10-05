package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomStatusReportTest {

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
        hotel.addRoom(new Room("104", Location.STANDARD, RoomStatus.AVAILABLE, standardType));
    }

    /**
     * UC-6: Generate Room Status Reports
     */
    @Test
    @DisplayName("UC-6 Test 1: Report summarizes total, occupied, vacant, and cleaning rooms")
    void testReportSummarizesRoomCounts() {
        controller.updateRoomStatus("102", RoomStatus.BOOKED);
        controller.triggerCleaningAlert("103");

        String report = controller.generateRoomStatusReport();

        assertTrue(report.contains("Total Rooms: 4"), report);
        assertTrue(report.contains("Occupied Rooms: 1"), report);
        assertTrue(report.contains("Vacant Rooms: 2"), report);
        assertTrue(report.contains("Rooms Under Cleaning: 1"), report);
    }

    @Test
    @DisplayName("UC-6 Test 2: Report calculates occupancy rate percentage")
    void testReportCalculatesOccupancyRate() {
        assertTrue(controller.generateRoomStatusReport().contains("Occupancy Rate: 0.0%"));

        controller.updateRoomStatus("101", RoomStatus.BOOKED);

        String report = controller.generateRoomStatusReport();
        assertTrue(report.contains("Occupancy Rate: 25.0%"), report);
    }
}
