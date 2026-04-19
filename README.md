# 🌊 Luqora - Water Quality Anomaly Detector (Backend)

This is the Spring Boot REST API backend for the **Luqora** water quality monitoring system. It handles secure data processing, user authentication, and provides endpoints for real-time sensor data and bulk CSV uploads.

## 🚀 Tech Stack
* **Framework:** Java / Spring Boot 3
* **Security:** Spring Security + JWT (JSON Web Tokens)
* **Database:** PostgreSQL (via Spring Data JPA)
* **Build Tool:** Maven

## 📂 Project Structure

A clean, MVC-based architecture for scalability and maintainability:

```text
waterquality/
 ├── src/main/java/com/ghost/waterquality/
 │    ├── controller/              # REST API Endpoints
 │    │    ├── AuthController.java       # Handles Login & Token Generation
 │    │    └── WaterReadingController.java # CRUD operations & CSV Uploads
 │    ├── models/                  # Database Entities
 │    │    ├── AnomalyAlert.java
 │    │    ├── User.java
 │    │    └── WaterReading.java
 │    ├── service/                 # Core Business Logic
 │    │    └── DataAnalyzer.java
 │    ├── util/                    # Helper Classes
 │    │    ├── CsvReader.java
 │    │    └── JwtUtil.java
 │    ├── SecurityConfig.java      # Spring Security & CORS Configuration
 │    ├── UserRepository.java      # JPA Data Access for Users
 │    ├── WaterReadingRepository.java # JPA Data Access for Readings
 │    └── WaterqualityApplication.java # Main Application Runner
 ├── src/main/resources/           # App configs (application.properties)
 └── pom.xml                       # Maven Dependencies
