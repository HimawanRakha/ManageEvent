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
    private Date date;
    private String time;
    private String location;
    
    private List<String> images;
    
    private String spsLink;

    private String creatorId; 

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}