package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Represents a room reservation made by a guest.
 * As defined in full_class_diagram.txt.
 */
public class Reservation {
    private String confirmationNumber;
    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalCost;

    public Reservation(String confirmationNumber, Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("Check-in and check-out dates cannot be null");
        }
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        this.confirmationNumber = confirmationNumber;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalCost = calculateTotalCost();
    }

    /**
     * Calculates the total cost based on nightly rate and number of nights.
     * Method specified in full_class_diagram.txt: calculateTotalCost() double
     */
    public double calculateTotalCost() {
        if (room != null && room.getRoomType() != null && checkInDate != null && checkOutDate != null) {
            long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            if (nights <= 0) {
                nights = 1;
            }
            return nights * room.getRoomType().getNightlyRate();
        }
        return 0.0;
    }

    /**
     * Cancels the reservation and releases the room back to AVAILABLE.
     * Method specified in full_class_diagram.txt: cancelReservation()
     */
    public void cancelReservation() {
        if (room != null && room.getStatus() == RoomStatus.BOOKED) {
            room.updateStatus(RoomStatus.AVAILABLE);
            room.setGuestName(null);
        }
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "confirmationNumber='" + confirmationNumber + '\'' +
                ", guest=" + guest.getName() +
                ", room=" + room.getRoomNumber() +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", totalCost=" + totalCost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(confirmationNumber, that.confirmationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmationNumber);
    }
}
