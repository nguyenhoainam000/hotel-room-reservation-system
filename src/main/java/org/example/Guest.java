package org.example;

import java.util.Objects;

/**
 * Represents a hotel guest.
 * As defined in full_class_diagram.txt.
 */
public class Guest {
    private String name;
    private String phone;

    public Guest(String name, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty");
        }
        this.name = name.trim();
        this.phone = phone != null ? phone.trim() : "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty");
        }
        this.name = name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone != null ? phone.trim() : "";
    }

    @Override
    public String toString() {
        return "Guest{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Objects.equals(name, guest.name) && Objects.equals(phone, guest.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }
}
