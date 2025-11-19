package com.example.ManageEvent.dto;

import lombok.Data; // Pastikan pakai Lombok, atau buat Getter/Setter manual

@Data
public class RegistrantResponse {
    private String userId;
    private String name;
    private String email;

    // Constructor manual (jika Lombok bermasalah)
    public RegistrantResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    // Constructor dengan userId
    public RegistrantResponse(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}