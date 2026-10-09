# 🏨 Hotel Room Reservation System - v1.0.0 (Official Release)

Welcome to the **v1.0.0 Official Release** of the Hotel Room Reservation System!
This release marks the final culmination of our Agile/Scrum development across Sprints 1 through 4 for **Software Engineering (Team 7 / Team 203)**.

---

## 🌟 What's New in v1.0.0

### 🚀 Core Business Functionality Delivered (UC-1 to UC-6):
1. **UC-1: View Room Availability:**
   - Real-time room status tracking (`AVAILABLE`, `BOOKED`, `CLEANING`, `OUT_OF_SERVICE`).
2. **UC-2: Room Selection by Size & Location:**
   - Multi-criteria room filtering by category (Standard, Deluxe, Executive Suite) and physical location (Pool Side, Balcony, Standard) with wildcard support.
3. **UC-3: Book Rooms for Guests (Create Reservation):**
   - End-to-end reservation workflow: guest profile verification, date range checks, stay cost calculation, unique confirmation code (`RES-XXXXXXXX`) generation, and Alternate Flow 5a input validation.
4. **UC-4: Cancel Room Reservations:**
   - Booking cancellation by confirmation number, automatic room status restoration to `AVAILABLE`, and Alternate Flow 4a invalid code handling.
5. **UC-5: Room Preparation & Cleaning Alert:**
   - Housekeeping turnover workflow: triggers cleaning alert setting room to `CLEANING` (locked from booking), followed by inspection and return to `AVAILABLE`.
6. **UC-6: Generate Room Status Reports:**
   - Consolidated hotel inventory report with room details, total count, occupied count, vacant count, cleaning count, and real-time occupancy rate percentage.

---

### 🖥️ Interactive Console Navigation Menu (`Main.java`):
- Run directly via executable JAR: `java -jar hotel-room-reservation-system-1.0.0.jar`
- 8 intuitive operational choices (UC-1 through UC-6, automated scenario demo, interactive loop, and graceful exit).

---

### 🧪 Automated Testing & Continuous Integration:
- **20 JUnit 5 Unit Tests** covering sunny day flows and edge cases (`CancelReservationTest`, `HotelReservationServiceTest`, `RoomSearchTest`, `RoomStatusReportTest`).
- **100% Pass Rate (BUILD SUCCESS)** via Maven Surefire runner.
- **Continuous Integration:** Automated build and test pipeline via GitHub Actions on Ubuntu with JDK 17.

---

## 👥 Team 7 Roles & Contributions:
- **Coleman Smalls (@ColeDakota)**: Product Owner (SME)
- **John Hashim (@shimspedy)**: Scrum Master
- **Johnathan Youngquist (@JohnYoungquist)**: Developer 1 (UC-2)
- **Quoc Nguyen / Nguyen Hoai Nam (@nguyenhoainam000)**: Developer 2 (Domain Architecture, UC-1, UC-3, UC-4, UC-5, UC-6, CLI Menu, 20 Unit Tests & CI Pipeline)
- **Jonathan Picardo**: Developer 3

---

## 📦 How to Run:
```bash
# Run the executable JAR directly
java -jar hotel-room-reservation-system-1.0.0.jar
```
