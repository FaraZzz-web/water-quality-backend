package com.ghost.waterquality.util; // Make sure this matches your exact folder name!

import com.ghost.waterquality.models.WaterReading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    // Method to read a CSV file and return a list of WaterReading objects
    public List<WaterReading> loadReadingsFromCsv(String filePath) {
        List<WaterReading> readings = new ArrayList<>();
        String line;

        // This is a "try-with-resources" block. It safely opens the file and auto-closes it when done.
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            // 1. Read and skip the very first line (because it's usually the column headers)
            br.readLine();

            // 2. Loop through every remaining line in the file until there are no more left
            while ((line = br.readLine()) != null) {

                // 3. Chop the line into pieces wherever there is a comma
                String[] values = line.split(",");

                // 4. Extract the pieces and convert them into the correct data types
                String location = values[0];
                double ph = Double.parseDouble(values[1]);
                double turbidity = Double.parseDouble(values[2]);
                double temperature = Double.parseDouble(values[3]);
                double dissolvedOxygen = Double.parseDouble(values[4]);

                // (We'll auto-generate the timestamp for now to keep the parsing simple)
                LocalDateTime timestamp = LocalDateTime.now();

                // 5. Build a new WaterReading object and throw it into our list
                WaterReading reading = new WaterReading(location, ph, turbidity, temperature, dissolvedOxygen, timestamp);
                readings.add(reading);
            }

            System.out.println("✅ Successfully loaded " + readings.size() + " readings from CSV!");

        } catch (IOException e) {
            // If the file doesn't exist or is locked, Java jumps here instead of crashing the whole app
            System.out.println("❌ ERROR: Could not read the file. " + e.getMessage());
        }

        return readings;
    }
}
