package org.example;

import java.util.Objects;

/**
 * Represents the category and pricing of a hotel room.
 * As defined in full_class_diagram.txt.
 */
public class RoomType {
    private String name;
    private String bedSize;
    private double nightlyRate;

    public RoomType(String name, String bedSize, double nightlyRate) {
        if (nightlyRate < 0) {
            throw new IllegalArgumentException("Nightly rate cannot be negative");
        }
        this.name = name;
        this.bedSize = bedSize;
        this.nightlyRate = nightlyRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBedSize() {
        return bedSize;
    }

    public void setBedSize(String bedSize) {
        this.bedSize = bedSize;
    }

    public double getNightlyRate() {
        return nightlyRate;
    }

    public void setNightlyRate(double nightlyRate) {
        if (nightlyRate < 0) {
            throw new IllegalArgumentException("Nightly rate cannot be negative");
        }
        this.nightlyRate = nightlyRate;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "name='" + name + '\'' +
                ", bedSize='" + bedSize + '\'' +
                ", nightlyRate=" + nightlyRate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomType roomType = (RoomType) o;
        return Double.compare(roomType.nightlyRate, nightlyRate) == 0 &&
                Objects.equals(name, roomType.name) &&
                Objects.equals(bedSize, roomType.bedSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, bedSize, nightlyRate);
    }
}
