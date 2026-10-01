package org.example;

import java.util.Objects;

/**
 * Represents a hotel room.
 * As defined in full_class_diagram.txt.
 */
public class Room {
    private String roomNumber;
    private Location location;
    private RoomStatus status;
    private RoomType roomType;
    private String guestName;

    public Room(String roomNumber, Location location, RoomStatus status, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.location = (location != null) ? location : Location.STANDARD;
        this.status = (status != null) ? status : RoomStatus.AVAILABLE;
        this.roomType = roomType;
        this.guestName = null;
    }

    public Room(String roomNumber, Location location, RoomType roomType) {
        this(roomNumber, location, RoomStatus.AVAILABLE, roomType);
    }

    /**
     * Convenience constructor for simple initialization.
     */
    public Room(String roomNumber, String typeName) {
        this(roomNumber, Location.STANDARD, RoomStatus.AVAILABLE, new RoomType(typeName, "Standard", 100.0));
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * Alias for getRoomNumber() for backward compatibility.
     */
    public String getRoomId() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    /**
     * Updates the status of the room.
     * Method specified in full_class_diagram.txt: updateStatus(RoomStatus newStatus)
     */
    public void updateStatus(RoomStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Room status cannot be null");
        }
        this.status = newStatus;
    }

    /**
     * Checks if the room is available for booking.
     * Method specified in full_class_diagram.txt: isAvailable() boolean
     */
    public boolean isAvailable() {
        return this.status == RoomStatus.AVAILABLE;
    }

    /**
     * Checks if the room is ready for a guest.
     * Method specified in full_class_diagram.txt: isReadyForGuest() boolean
     */
    public boolean isReadyForGuest() {
        return this.status == RoomStatus.AVAILABLE;
    }

    public boolean isNeedsCleaning() {
        return this.status == RoomStatus.CLEANING;
    }

    public void setNeedsCleaning(boolean needsCleaning) {
        this.status = needsCleaning ? RoomStatus.CLEANING : RoomStatus.AVAILABLE;
    }

    public void setAvailable(boolean available) {
        this.status = available ? RoomStatus.AVAILABLE : RoomStatus.BOOKED;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", location=" + location +
                ", status=" + status +
                ", roomType=" + (roomType != null ? roomType.getName() : "None") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomNumber, room.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }
}
