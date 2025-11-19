package com.example.ManageEvent.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String adminCode; // Menangkap kode admin dari frontend
}