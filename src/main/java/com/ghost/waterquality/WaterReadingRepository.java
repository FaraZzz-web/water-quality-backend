package com.ghost.waterquality; // Update this to match your folder structure

import com.ghost.waterquality.models.WaterReading; // Make sure this points to your Entity!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterReadingRepository extends JpaRepository<WaterReading, Long> {
    // Leave this completely empty!
    // JpaRepository automatically gives you save(), findAll(), findById(), etc.
}