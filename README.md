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

## 📋 Project Backlog & Sprint Management
Project backlog, user story tracking, and iteration progress are managed on GitHub Projects:  
🔗 **[Backlog · Room Reservation System for a Hotel](https://github.com/users/ColeDakota/projects/1/views/1)**

### Core User Stories & Features:
* **UC-1: Book Rooms for Guests:** Allows guests to reserve available rooms with validation.
* **UC-2: View Room Availability:** Real-time visibility into room vacancy and status.
* **UC-3: Room Preparation & Cleaning Alert:** Automated notification workflow for housekeeping when rooms require turnover.
* **UC-4: Room Selection by Size & Location:** Filters rooms by capacity, room type, and floor/wing.
* **UC-5: Cancel Room Reservations:** Handles booking cancellations and frees rooms back into inventory.
* **UC-6: Update Room Status:** Administrative controls for room maintenance and readiness.
* **UC-7: Generate Room Status Reports:** Management reporting and occupancy metrics.

---

## 🏗️ Architecture & Tech Stack
* **Language:** Java 17 LTS
* **Build Tool:** Apache Maven
* **Testing:** JUnit 5 (Jupiter)
* **CI/CD:** GitHub Actions (`actions/checkout@v5`, `actions/setup-java@v5`)
* **Design Patterns:** Service Layer, Separation of Concerns (Model - Service)

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

# Package the project
mvn clean package
```

---

## 🔄 Automated CI/CD
This repository is configured with **GitHub Actions Continuous Integration**. Every `push` and `pull_request` to the `main` branch automatically triggers:
1. Environment provisioning on `ubuntu-latest`.
2. JDK 17 setup with dependency caching.
3. Automated compilation and execution of unit tests via Maven Surefire.
