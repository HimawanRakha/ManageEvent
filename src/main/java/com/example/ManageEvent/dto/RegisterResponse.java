package com.example.ManageEvent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private String message;
    private String id;
    private String name;
    private String email;
    private String role;
}

