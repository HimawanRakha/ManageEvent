package com.example.ManageEvent.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "events")
public class Event {
    @Id
    private String id;

    private String title;
    private String description;
    private Date date; // Bisa pakai java.util.Date atau java.time.LocalDateTime
    private String time; // Waktu event (format: "HH:mm" atau "HH:mm:ss")
    private String location;
    
    private List<String> images; // Array of strings
    
    private String spsLink; // Optional otomatis handled by Java (bisa null)

    // Kita simpan ID-nya saja untuk referensi
    private String creatorId; 

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}