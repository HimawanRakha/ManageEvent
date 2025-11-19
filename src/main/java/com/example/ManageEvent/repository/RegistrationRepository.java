package com.example.ManageEvent.repository;

import com.example.ManageEvent.model.Registration;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface RegistrationRepository extends MongoRepository<Registration, String> {
    // Custom method: Cek apakah User sudah daftar di Event tertentu
    Optional<Registration> findByUserIdAndEventId(String userId, String eventId);
    
    // Custom method: Cari semua registrasi untuk Event tertentu
    List<Registration> findByEventId(String eventId);
    
    // Custom method: Cari semua registrasi untuk User tertentu
    List<Registration> findByUserId(String userId);
}