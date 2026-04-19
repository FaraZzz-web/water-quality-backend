package com.ghost.waterquality;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.ghost.waterquality") // Forces Spring to find your @Entity
@EnableJpaRepositories(basePackages = "com.ghost.waterquality") // Forces Spring to find your Repository
public class WaterqualityApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaterqualityApplication.class, args);
    }
}