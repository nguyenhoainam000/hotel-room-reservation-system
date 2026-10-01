package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== HOTEL ROOM RESERVATION SYSTEM ===");
        HotelReservationService service = new HotelReservationService();

        service.addRoom(new Room("101", "Deluxe"));
        service.addRoom(new Room("102", "Standard"));
        service.addRoom(new Room("103", "Suite"));

        System.out.println("Room 101 Available: " + service.viewRoomAvailability("101"));

        System.out.println("Booking Room 101 for 'Nguyen Hoai Nam'...");
        boolean booked = service.bookRoom("101", "Nguyen Hoai Nam");
        System.out.println("Booking Success: " + booked);
        System.out.println("Room 101 Available after booking: " + service.viewRoomAvailability("101"));

        System.out.println("Triggering cleaning alert for Room 102...");
        service.triggerCleaningAlert("102");
        System.out.println("Room 102 Available while cleaning: " + service.viewRoomAvailability("102"));
    }
}