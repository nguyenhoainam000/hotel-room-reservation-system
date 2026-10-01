package org.example;

public class Room {
    private String roomId;
    private String roomType;
    private boolean isAvailable;
    private boolean needsCleaning;
    private String guestName;

    public Room(String roomId, String roomType) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.isAvailable = true;
        this.needsCleaning = false;
        this.guestName = null;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isAvailable() {
        return isAvailable && !needsCleaning;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isNeedsCleaning() {
        return needsCleaning;
    }

    public void setNeedsCleaning(boolean needsCleaning) {
        this.needsCleaning = needsCleaning;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
}
