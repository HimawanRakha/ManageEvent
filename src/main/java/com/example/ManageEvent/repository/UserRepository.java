package com.example.ManageEvent.repository;

import com.example.ManageEvent.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // Custom method untuk mencari User berdasarkan Email
    Optional<User> findByEmail(String email);
}