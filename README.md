# Room Reservation System for a Hotel

![Java CI](https://github.com/nguyenhoainam000/hotel-room-reservation-system/actions/workflows/ci.yml/badge.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java Version](https://img.shields.io/badge/Java-17%20LTS-orange.svg)
![Build](https://img.shields.io/badge/build-Maven-red.svg)

A modular, enterprise-grade Hotel Room Reservation System developed using Agile/Scrum methodology for **Software Engineering (Team 7 / Team 203)**.

---

## 👥 Team 7 Information & Roles

| Member | Role | Responsibilities |
| :--- | :--- | :--- |
| **Coleman Smalls** (`ColeDakota`) | **Product Owner (SME)** | Backlog ownership, feature definitions & acceptance criteria, domain expert. |
| **John Hashim** | **Scrum Master** | Agile ceremony facilitation, blocker removal, weekly reporting (*Team Assignment 3*). |
| **Johnathan Youngquist** | **Developer 1** | Use case analysis, domain modeling, Java implementation. |
| **Quoc Nguyen / Nguyen Hoai Nam** (`nguyenhoainam000`) | **Developer 2** | Use case & diagram analysis, core Java implementation, unit testing & CI/CD. |
| **Jonathan Picardo** | **Developer 3** | Glossary, testing, Java implementation, documentation. |

---

## 📋 Project Backlog & Assigned User Stories
Project backlog, user story tracking, and iteration progress are managed on GitHub Projects:  
🔗 **[Backlog · Room Reservation System for a Hotel](https://github.com/users/ColeDakota/projects/1/views/1)**

### Assigned Scope (Developer 2 - Quoc Nguyen / `nguyenhoainam000`):
* ✅ **UC-1: View Room Availability:** Real-time visibility into room vacancy and status (`AVAILABLE`, `BOOKED`, `CLEANING`, `OUT_OF_SERVICE`).
* ✅ **UC-3: Book Rooms for Guests (Create Reservation):** Reserves available rooms, calculates total stay cost based on nightly rates, assigns confirmation numbers, updates room status to `BOOKED`, and validates input (rejects blank guest names per Sequence Diagram Alternate Flow 5a).
* ✅ **UC-5: Room Preparation & Cleaning Alert (Update Room Status):** Housekeeping workflow to trigger cleaning alerts on turnover, setting room to `CLEANING` (making it unbookable), and restoring to `AVAILABLE` once inspected and prepared.

### Team Backlog User Stories (Other Members):
* **UC-2: Search Rooms by Type and Location** (Developer 1)
* **UC-4: Cancel Reservation** (Developer 1 / Developer 3)
* **UC-6: Generate Room Status Report** (Developer 3)

---

## 🏛️ Domain Architecture & Design Conformance
The implementation strictly conforms to the UML design specifications (`full_class_diagram.txt` and `sequence_diagram.txt`):

* **Enums:**
  * `Location`: `POOL_SIDE`, `BALCONY`, `STANDARD`
  * `RoomStatus`: `AVAILABLE`, `BOOKED`, `CLEANING`, `OUT_OF_SERVICE`
* **Domain Models:**
  * `Hotel`: Manages hotel details and room inventory (`addRoom`, `getRoom`).
  * `Room`: Represents rooms with status, type, and location (`updateStatus`, `isAvailable`, `isReadyForGuest`).
  * `RoomType`: Defines room category, bed size, and nightly rate (`getNightlyRate`, `getBedSize`).
  * `Guest`: Encapsulates guest profile (`getName`, `getPhone`) with validation.
  * `Reservation`: Encapsulates confirmed bookings, dates, and cost (`calculateTotalCost`, `cancelReservation`).
* **Controllers & Services:**
  * `HotelController`: Orchestrates operations between UI and domain models per UML class diagram.
  * `HotelReservationService`: Service facade providing business workflows and backwards-compatible APIs.

---

## 🏗️ Architecture & Tech Stack
* **Language:** Java 17 LTS
* **Build Tool:** Apache Maven
* **Testing:** JUnit 5 (Jupiter) with parameterized tests & alternate flow coverage
* **CI/CD:** GitHub Actions (`actions/checkout@v5`, `actions/setup-java@v5`)
* **Design Patterns:** Controller / Service Layer, Separation of Concerns (Model - Controller - Service)

---

## 🚀 Getting Started

### Prerequisites
* JDK 17 or higher
* Apache Maven 3.8+ (or IntelliJ IDEA built-in Maven)

### Clone the Repository
```bash
git clone https://github.com/nguyenhoainam000/hotel-room-reservation-system.git
cd hotel-room-reservation-system
```

### Build & Run Tests
```bash
# Compile and run all unit tests
mvn clean test

# Run demo application
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```

---

## 🔄 Automated CI/CD
This repository is configured with **GitHub Actions Continuous Integration**. Every `push` and `pull_request` to the `main` branch automatically triggers:
1. Environment provisioning on `ubuntu-latest`.
2. JDK 17 setup with dependency caching.
3. Automated compilation and execution of unit tests via Maven Surefire.
