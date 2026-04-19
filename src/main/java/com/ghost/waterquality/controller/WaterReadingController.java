package com.ghost.waterquality.controller;

import com.ghost.waterquality.models.WaterReading;
import com.ghost.waterquality.WaterReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/readings")
public class WaterReadingController {

    @Autowired
    private WaterReadingRepository repository;

    @GetMapping
    public List<WaterReading> getAllReadings() {
        System.out.println("🌐 Fetching real data from PostgreSQL...");
        return repository.findAll();
    }

    @GetMapping("/test-add")
    public String addTestData() {
        WaterReading testData = new WaterReading(
                "Delhi-Yamuna River",
                7.2,
                4.5,
                26.0,
                6.8,
                LocalDateTime.now()
        );
        repository.save(testData);
        return "Data saved successfully! Go check pgAdmin!";
    }

    // ----------------------------------------------------
    // POST: Add a new reading
    // ----------------------------------------------------
    @PostMapping
    public WaterReading addReading(@RequestBody WaterReading newReading) {
        System.out.println("📥 Receiving new water data for: " + newReading.getLocation());

        if (newReading.getTimestamp() == null) {
            newReading.setTimestamp(LocalDateTime.now());
        }

        // FIX: We removed the old hardcoded "ANOMALY" logic here!
        // We now fully trust the advanced "CRITICAL"/"SAFE" status and Valve state
        // that the React Dashboard + Python AI sends us in the JSON payload.

        return repository.save(newReading);
    }

    @PostMapping("/bulk")
    public List<WaterReading> addMultipleReadings(@RequestBody List<WaterReading> newReadings) {
        System.out.println("📥 Receiving BULK water data! Count: " + newReadings.size());
        for (WaterReading reading : newReadings) {
            if (reading.getTimestamp() == null) {
                reading.setTimestamp(LocalDateTime.now());
            }
            // Basic fallback for bulk uploads bypassing the AI frontend
            boolean isPhBad = reading.getPh() < 6.5 || reading.getPh() > 8.5;
            boolean isTurbidityBad = reading.getTurbidity() > 5.0;
            if (isPhBad || isTurbidityBad) {
                reading.setStatus("ANOMALY");
            } else {
                reading.setStatus("NORMAL");
            }
        }
        return repository.saveAll(newReadings);
    }

    // ----------------------------------------------------
    // PUT: The REAL API Endpoint for changing existing data
    // ----------------------------------------------------
    @PutMapping("/{id}")
    public WaterReading updateReading(@PathVariable Long id, @RequestBody WaterReading updatedData) {
        System.out.println("🔄 Updating water data for ID: " + id);

        WaterReading existingData = repository.findById(id).orElseThrow(() -> new RuntimeException("Data not found!"));

        // 1. Update the basic sensor numbers
        existingData.setLocation(updatedData.getLocation());
        existingData.setPh(updatedData.getPh());
        existingData.setTurbidity(updatedData.getTurbidity());
        existingData.setTemperature(updatedData.getTemperature());
        existingData.setDissolvedOxygen(updatedData.getDissolvedOxygen());

        // 2. THE FIX: Explicitly tell Java to update the AI-driven fields too!
        existingData.setTimestamp(updatedData.getTimestamp());
        existingData.setStatus(updatedData.getStatus());
        existingData.setValveClosed(updatedData.getValveClosed());

        return repository.save(existingData);
    }

    @DeleteMapping("/{id}")
    public String deleteReading(@PathVariable Long id) {
        System.out.println("❌ Deleting water data for ID: " + id);
        repository.deleteById(id);
        return "Data with ID " + id + " has been successfully deleted!";
    }

    @PostMapping("/analyze")
    public String analyzeImage(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        System.out.println("📸 Received image for AI analysis: " + file.getOriginalFilename());
        try {
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            String pythonApiUrl = "http://localhost:5000/predict";

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);

            org.springframework.util.MultiValueMap<String, Object> body = new org.springframework.util.LinkedMultiValueMap<>();
            body.add("file", file.getResource());

            org.springframework.http.HttpEntity<org.springframework.util.MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(body, headers);

            System.out.println("⏳ Sending image to Python AI...");
            org.springframework.http.ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);

            System.out.println("🧠 AI Response: " + response.getBody());
            return response.getBody();

        } catch (Exception e) {
            System.out.println("❌ Error communicating with Python: " + e.getMessage());
            return "{\"error\": \"Failed to connect to AI Microservice. Is the Python server running?\"}";
        }
    }
    // 👇 YAHAN SE NAYA CODE COPY KAR 👇
    // ----------------------------------------------------
    // POST: Upload and parse CSV file
    // ----------------------------------------------------
    @PostMapping("/upload")
    public org.springframework.http.ResponseEntity<String> uploadCsvFile(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {

        System.out.println("📄 Received CSV file: " + file.getOriginalFilename());

        try {
            // Hum ek CSV parsing logic likhenge yahan (abhi ke liye bas check kar rahe hain request aayi ya nahi)
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(file.getInputStream()));
            String line;
            java.util.List<WaterReading> readings = new java.util.ArrayList<>();

            // Skip header (First line)
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                // Assuming CSV structure: Location,pH,Turbidity,Temperature,DissolvedOxygen
                String[] data = line.split(",");
                if (data.length >= 5) {
                    WaterReading reading = new WaterReading();
                    reading.setLocation(data[0]);
                    reading.setPh(Double.parseDouble(data[1]));
                    reading.setTurbidity(Double.parseDouble(data[2]));
                    reading.setTemperature(Double.parseDouble(data[3]));
                    reading.setDissolvedOxygen(Double.parseDouble(data[4]));
                    reading.setTimestamp(LocalDateTime.now());

                    // Simple logic for status
                    if (reading.getPh() < 6.5 || reading.getPh() > 8.5 || reading.getTurbidity() > 5.0) {
                        reading.setStatus("ANOMALY");
                    } else {
                        reading.setStatus("NORMAL");
                    }

                    readings.add(reading);
                }
            }

            // Save all rows to database
            repository.saveAll(readings);
            System.out.println("✅ Saved " + readings.size() + " readings to the database.");

            return org.springframework.http.ResponseEntity.ok("CSV Processed Successfully!");

        } catch (Exception e) {
            System.out.println("❌ CSV Processing Error: " + e.getMessage());
            return org.springframework.http.ResponseEntity.status(500).body("Error processing file");
        }
    }
} // End of WaterReadingController class
