package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the Hotel entity containing rooms.
 * As defined in full_class_diagram.txt.
 */
public class Hotel {
    private String name;
    private String address;
    private final List<Room> rooms;

    public Hotel(String name, String address) {
        this.name = name;
        this.address = address;
        this.rooms = new ArrayList<>();
    }

    /**
     * Adds a room to the hotel inventory.
     * Method specified in full_class_diagram.txt: addRoom(Room room)
     */
    public void addRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        rooms.add(room);
    }

    /**
     * Finds a room by its room number.
     * Method specified in full_class_diagram.txt: getRoom(String roomNumber) Room
     */
    public Room getRoom(String roomNumber) {
        if (roomNumber == null) return null;
        return rooms.stream()
                .filter(r -> r.getRoomNumber().equalsIgnoreCase(roomNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
