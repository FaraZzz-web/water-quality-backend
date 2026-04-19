package com.ghost.waterquality;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // API ke liye CSRF disable karna zaroori hai
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll() // Login page sabke liye khula hai
                        .anyRequest().permitAll() // Abhi ke liye baaki sab open rakha hai, next step mein lock karenge!
                );
        return http.build();
    }
    // 👇 2. YAHAN CORS KI SETTINGS BATA 👇
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Tera React Frontend ka URL
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));

        // OPTIONS method ko allow karna sabse zaroori hai preflight ke liye
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Authorization aur Content-Type headers ko allow kar
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Ye setting poore project (/**) par lagu hogi
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}