> **Individual Assignment Submission**  
> **Course:** Software Engineering  
> **Student:** Nguyen Hoai Nam (`nguyenhoainam000`)  
> **Focus:** Automated Testing with GitHub Actions (CI) & Unit Testing with JUnit 5

---

## 📌 Overview
This repository contains the implementation of core hotel reservation logic and automated unit tests developed as part of **Individual Assignment 4** (Setup Automated Testing using GitHub Actions) and **Assignment 2** (Unit Testing for User Stories).

### Assigned User Stories Implemented:
1. **Book Rooms for Guests:** Reserves a specified available room for a guest.
2. **View Room Availability:** Checks whether a room is ready and vacant for reservation.
3. **Room Preparation and Cleaning Alert:** Triggers housekeeping alerts when a room requires cleaning before being released for new guests.

---

## 🛠️ Tech Stack & Requirements
* **Language:** Java 17
* **Build System:** Apache Maven
* **Testing Framework:** JUnit 5 (Jupiter)
* **CI/CD Pipeline:** GitHub Actions (`.github/workflows/ci.yml`)

---

## 🧪 Running Unit Tests Locally
To execute the unit tests locally with Maven:
```bash
mvn clean test
```

Or open the project in IntelliJ IDEA, navigate to `src/test/java/org/example/HotelReservationServiceTest.java` and click the green run icon next to the class name.

---

## 🚀 Automated CI/CD Pipeline
Every push and pull request to the `main` branch automatically triggers the **GitHub Actions workflow**, which:
1. Sets up an **Ubuntu** environment with **JDK 17**.
2. Compiles and packages the application using Maven.
3. Executes all unit tests automatically to ensure software quality.