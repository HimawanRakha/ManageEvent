package com.example.ManageEvent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String id;
    private String name;
    private String email;
    private String role;
    // private String token;
}