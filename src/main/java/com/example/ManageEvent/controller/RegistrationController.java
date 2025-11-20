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
        List<Registration> registrations = registrationRepository.findByEventId(eventId);
        List<RegistrantResponse> responseList = new ArrayList<>();
        for (Registration reg : registrations) {
            Optional<User> userOpt = userRepository.findById(reg.getUserId());
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                responseList.add(new RegistrantResponse(user.getId(), user.getName(), user.getEmail()));
            } else {
                responseList.add(new RegistrantResponse(reg.getUserId(), "Unknown User", "No Email"));
            }
        }

        return responseList;
    }

    @PostMapping
    public ResponseEntity<?> registerUserForEvent(@RequestBody Registration registration) {
        if (registrationRepository.findByUserIdAndEventId(registration.getUserId(), registration.getEventId()).isPresent()) {
            return new ResponseEntity<>("User already registered for this event.", HttpStatus.CONFLICT);
        }
        
        try {
            Registration savedRegistration = registrationRepository.save(registration);
            return new ResponseEntity<>(savedRegistration, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>("User already registered for this event (DB check).", HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping("/my-events")
    public ResponseEntity<List<Event>> getMyEvents(@RequestParam String userId) {
        List<Registration> registrations = registrationRepository.findByUserId(userId);
        List<String> eventIds = registrations.stream()
                .map(Registration::getEventId)
                .collect(Collectors.toList());
        
        List<Event> events = eventRepository.findAllById(eventIds);
        return ResponseEntity.ok(events);
    }

    @DeleteMapping
    public ResponseEntity<?> cancelRegistration(@RequestParam String userId, @RequestParam String eventId) {
        Optional<Registration> registrationOpt = registrationRepository.findByUserIdAndEventId(userId, eventId);
        
        if (registrationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        registrationRepository.delete(registrationOpt.get());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkRegistration(@RequestParam String userId, @RequestParam String eventId) {
        boolean isRegistered = registrationRepository.findByUserIdAndEventId(userId, eventId).isPresent();
        return ResponseEntity.ok(isRegistered);
    }
}