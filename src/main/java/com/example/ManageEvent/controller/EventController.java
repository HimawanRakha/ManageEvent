package com.example.ManageEvent.controller;

import com.example.ManageEvent.model.Event;
import com.example.ManageEvent.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000") 
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        return eventRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // @PostMapping
    // public ResponseEntity<Event> createEvent(@RequestBody Event event) {
    //     // Logika: Creator ID harus diambil dari User yang sedang login (membutuhkan Spring Security)
    //     // Untuk saat ini, kita simpan saja
    //     Event savedEvent = eventRepository.save(event);
    //     return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    // }
    
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        // Di sini nanti bisa tambah logika validasi creatorId dari User yang login
        return eventRepository.save(event);
    }
    
    // PUT: Update event
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable String id, @RequestBody Event event) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    existingEvent.setTitle(event.getTitle());
                    existingEvent.setDescription(event.getDescription());
                    existingEvent.setDate(event.getDate());
                    existingEvent.setTime(event.getTime());
                    existingEvent.setLocation(event.getLocation());
                    existingEvent.setImages(event.getImages());
                    existingEvent.setSpsLink(event.getSpsLink());
                    Event updatedEvent = eventRepository.save(existingEvent);
                    return ResponseEntity.ok(updatedEvent);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // DELETE
   @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable String id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        eventRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}