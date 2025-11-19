package com.example.ManageEvent.controller;

import com.example.ManageEvent.dto.AuthResponse;
import com.example.ManageEvent.dto.ErrorResponse;
import com.example.ManageEvent.dto.LoginRequest;
import com.example.ManageEvent.dto.RegisterRequest;
import com.example.ManageEvent.dto.RegisterResponse;
import com.example.ManageEvent.model.User;
import com.example.ManageEvent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Izinkan Next.js akses
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Kita butuh encoder untuk hash password
    // Pastikan kamu sudah membuat Bean PasswordEncoder di konfigurasi (Lihat langkah 3 di bawah)
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // === REGISTER ===
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        // 1. Cek apakah email sudah ada
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Email is already taken!", "CONFLICT"));
        }

        // 2. Buat User baru
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        
        // 3. Hash Password (JANGAN SIMPAN PLAIN TEXT)
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 4. Logic Role berdasarkan Admin Code (case-insensitive dan handle null)
        String adminCode = request.getAdminCode();
        if (adminCode != null && adminCode.trim().equalsIgnoreCase("ADMIN123")) {
            user.setRole(User.Role.ADMIN);
        } else {
            user.setRole(User.Role.USER);
        }

        // 5. Simpan ke DB
        User savedUser = userRepository.save(user);

        // 6. Return JSON response dengan data user (tanpa password)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new RegisterResponse(
                "User registered successfully",
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
            )
        );
    }

    // === LOGIN ===
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        // 1. Cari User by Email
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid email or password", "UNAUTHORIZED"));
        }

        User user = userOpt.get();

        // 2. Cek Password (Raw vs Hash)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid email or password", "UNAUTHORIZED"));
        }

        // 3. Return data user (Response ini akan dipakai NextAuth di frontend)
        // Kita convert Enum Role ke String biar aman di JSON
        return ResponseEntity.ok(new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        ));
    }
}