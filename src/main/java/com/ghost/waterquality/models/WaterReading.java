package com.ghost.waterquality.models;

import jakarta.persistence.*; // Important: This is the library we added to pom.xml
import java.time.LocalDateTime;

@Entity // 1. Tells Spring Boot this class is a Database Table
@Table(name = "water_readings") // 2. Sets the name of the table in PostgreSQL
public class WaterReading {

    @Id // 3. Every database table needs a Primary Key (Unique ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. Auto-increments (1, 2, 3...)
    private Long id;

    private String location;
    private double ph;
    private double turbidity;
    private double temperature;
    private double dissolvedOxygen;
    private LocalDateTime timestamp;
    private String status;

    // FIX: Using 'Boolean' object instead of primitive 'boolean'.
    // This prevents crashes on old database rows that have this column as NULL!
    @Column(name = "valve_closed", columnDefinition = "boolean default false")
    private Boolean valveClosed = false;

    // 5. CRUCIAL: Hibernate needs an empty constructor to work!
    public WaterReading() {
    }

    public WaterReading(String location, double ph, double turbidity, double temperature, double dissolvedOxygen, LocalDateTime timestamp) {
        this.location = location;
        this.ph = ph;
        this.turbidity = turbidity;
        this.temperature = temperature;
        this.dissolvedOxygen = dissolvedOxygen;
        this.timestamp = timestamp;
        this.status = "PENDING";
        this.valveClosed = false;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public double getTurbidity() {
        return turbidity;
    }

    public void setTurbidity(double turbidity) {
        this.turbidity = turbidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getDissolvedOxygen() {
        return dissolvedOxygen;
    }

    public void setDissolvedOxygen(double dissolvedOxygen) {
        this.dissolvedOxygen = dissolvedOxygen;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Updated Getter and Setter for the new Boolean object
    public Boolean getValveClosed() {
        return valveClosed;
    }

    public void setValveClosed(Boolean valveClosed) {
        this.valveClosed = valveClosed;
    }
}