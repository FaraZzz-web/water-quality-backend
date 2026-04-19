package com.ghost.waterquality;

import com.ghost.waterquality.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Boot itna smart hai ki sirf ye line likhne se wo database mein
    // khud email search karne ka SQL query bana lega!
    Optional<User> findByEmail(String email);
}