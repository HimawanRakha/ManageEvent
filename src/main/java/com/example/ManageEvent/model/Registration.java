package com.example.ManageEvent.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data
@Document(collection = "registrations")
// Ini logic agar user tidak bisa daftar event yang sama 2x
@CompoundIndex(def = "{'userId': 1, 'eventId': 1}", unique = true) 
public class Registration {
    @Id
    private String id;

    private String userId;
    private String eventId;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}