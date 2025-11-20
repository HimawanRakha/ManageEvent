package com.example.ManageEvent.dto;

import lombok.Data;

@Data
public class RegistrantResponse {
    private String userId;
    private String name;
    private String email;

    public RegistrantResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public RegistrantResponse(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}