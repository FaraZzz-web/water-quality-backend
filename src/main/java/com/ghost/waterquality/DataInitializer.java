package com.ghost.waterquality;

import com.ghost.waterquality.models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@water.com";
        String adminPassword = "password123"; // Tu isko baad mein change kar sakta hai

        // Database mein check karo ki admin pehle se hai ya nahi
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            System.out.println("⚠️ Admin account nahi mila. Naya account bana raha hu...");

            User admin = new User();
            admin.setEmail(adminEmail);
            // Password ko BCrypt se encrypt karna BOHOT zaroori hai
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole("ADMIN");

            userRepository.save(admin);
            System.out.println("✅ Admin account successfully ban gaya!");
        } else {
            System.out.println("👍 Admin account pehle se database mein maujood hai.");
        }
    }
}