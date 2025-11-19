package com.example.ManageEvent.controller;

import com.example.ManageEvent.dto.RegistrantResponse; 
import com.example.ManageEvent.model.Event;
import com.example.ManageEvent.model.Registration;
import com.example.ManageEvent.model.User;
import com.example.ManageEvent.repository.EventRepository;
import com.example.ManageEvent.repository.RegistrationRepository;
import com.example.ManageEvent.repository.UserRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EventRepository eventRepository;

   @GetMapping("/event/{eventId}")
    public List<RegistrantResponse> getRegistrantsByEvent(@PathVariable String eventId) {
        
        // 1. Ambil semua data pendaftaran berdasarkan ID Event
        List<Registration> registrations = registrationRepository.findByEventId(eventId);
        
        // 2. Siapkan list kosong untuk menampung hasil
        List<RegistrantResponse> responseList = new ArrayList<>();

        // 3. Loop setiap pendaftaran, cari nama usernya
        for (Registration reg : registrations) {
            // Cari User berdasarkan userId yang ada di tabel Registration
            Optional<User> userOpt = userRepository.findById(reg.getUserId());
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // Masukkan ke format DTO (UserId, Nama & Email)
                responseList.add(new RegistrantResponse(user.getId(), user.getName(), user.getEmail()));
            } else {
                // Jika user sudah dihapus tapi datanya masih ada di registrasi
                responseList.add(new RegistrantResponse(reg.getUserId(), "Unknown User", "No Email"));
            }
        }

        return responseList;
    }

    // Endpoint untuk Mendaftar
    @PostMapping
    public ResponseEntity<?> registerUserForEvent(@RequestBody Registration registration) {
        // Validasi kustom (meskipun CompoundIndex sudah ada, ini memberi pesan yang lebih baik)
        if (registrationRepository.findByUserIdAndEventId(registration.getUserId(), registration.getEventId()).isPresent()) {
            return new ResponseEntity<>("User already registered for this event.", HttpStatus.CONFLICT);
        }
        
        try {
            Registration savedRegistration = registrationRepository.save(registration);
            return new ResponseEntity<>(savedRegistration, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
             // Ini seharusnya ditangkap oleh validasi di atas, tapi ini adalah fallback untuk CompoundIndex
            return new ResponseEntity<>("User already registered for this event (DB check).", HttpStatus.CONFLICT);
        }
    }
    
    // GET: Ambil semua event yang didaftar user
    @GetMapping("/my-events")
    public ResponseEntity<List<Event>> getMyEvents(@RequestParam String userId) {
        List<Registration> registrations = registrationRepository.findByUserId(userId);
        List<String> eventIds = registrations.stream()
                .map(Registration::getEventId)
                .collect(Collectors.toList());
        
        List<Event> events = eventRepository.findAllById(eventIds);
        return ResponseEntity.ok(events);
    }
    
    // DELETE: Batalkan pendaftaran
    @DeleteMapping
    public ResponseEntity<?> cancelRegistration(@RequestParam String userId, @RequestParam String eventId) {
        Optional<Registration> registrationOpt = registrationRepository.findByUserIdAndEventId(userId, eventId);
        
        if (registrationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        registrationRepository.delete(registrationOpt.get());
        return ResponseEntity.ok().build();
    }
    
    // GET: Cek status registrasi
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkRegistration(@RequestParam String userId, @RequestParam String eventId) {
        boolean isRegistered = registrationRepository.findByUserIdAndEventId(userId, eventId).isPresent();
        return ResponseEntity.ok(isRegistered);
    }
}