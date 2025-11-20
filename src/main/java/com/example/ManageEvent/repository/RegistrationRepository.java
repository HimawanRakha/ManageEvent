package com.example.ManageEvent.repository;

import com.example.ManageEvent.model.Registration;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface RegistrationRepository extends MongoRepository<Registration, String> {
    Optional<Registration> findByUserIdAndEventId(String userId, String eventId);
    List<Registration> findByEventId(String eventId);
    List<Registration> findByUserId(String userId);
}