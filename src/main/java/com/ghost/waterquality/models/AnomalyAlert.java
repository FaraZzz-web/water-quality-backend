package com.ghost.waterquality.models;

import java.time.LocalDateTime;

public class AnomalyAlert {
    private WaterReading reading;
    private double anomalyScore;
    private String riskLevel;
    private String affectedPopulation;
    private String recommendedActions;
    private LocalDateTime detectedAt;

    public AnomalyAlert(WaterReading reading, double anomalyScore) {
        this.reading = reading;
        this.anomalyScore = anomalyScore;
        this.detectedAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public WaterReading getReading() { return reading; }
    public void setReading(WaterReading reading) { this.reading = reading; }

    public double getAnomalyScore() { return anomalyScore; }
    public void setAnomalyScore(double anomalyScore) { this.anomalyScore = anomalyScore; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public String getAffectedPopulation() { return affectedPopulation; }
    public void setAffectedPopulation(String affectedPopulation) { this.affectedPopulation = affectedPopulation; }

    public String getRecommendedActions() { return recommendedActions; }
    public void setRecommendedActions(String recommendedActions) { this.recommendedActions = recommendedActions; }

    public LocalDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
}