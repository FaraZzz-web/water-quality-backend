package com.ghost.waterquality.controller;

import com.ghost.waterquality.UserRepository;
import com.ghost.waterquality.models.User;
import com.ghost.waterquality.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // React ko block hone se bachane ke liye
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. Database mein email dhoondho
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // 2. Password check karo
            if (user.getPassword().equals(request.getPassword())) {
                // 3. Password sahi hai toh Token banao aur React ko bhej do!
                String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
                return ResponseEntity.ok(Map.of("token", token, "message", "Login Successful"));
            }
        }
        // Agar email/password galat hai toh error bhejo
        return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
    }
}

// Chhoti si class React se aane wale email/password ko pakadne ke liye
class LoginRequest {
    private String email;
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}