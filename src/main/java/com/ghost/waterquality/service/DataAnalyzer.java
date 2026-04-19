package com.ghost.waterquality.service;

import com.ghost.waterquality.models.WaterReading;
import java.util.List;
import java.util.stream.Collectors;

public class DataAnalyzer {

    // Method to find anomalies based on high pH levels
    public List<WaterReading> findHighPhAnomalies(List<WaterReading> allReadings) {
        return allReadings.stream()
                .filter(reading -> reading.getPh() > 8.5)
                .collect(Collectors.toList());
    }
}
