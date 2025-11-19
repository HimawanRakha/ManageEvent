package com.example.ManageEvent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Ini yang membuat @Autowired BCryptPasswordEncoder di Controller bekerja
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Konfigurasi CORS untuk Next.js (Local & Production)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Daftar allowed origins (local development + production Vercel)
        // Bisa ditambahkan via environment variable ALLOWED_ORIGINS jika perlu
        String allowedOriginsEnv = System.getenv("ALLOWED_ORIGINS");
        if (allowedOriginsEnv != null && !allowedOriginsEnv.isEmpty()) {
            configuration.setAllowedOrigins(Arrays.asList(allowedOriginsEnv.split(",")));
        } else {
            // Default: localhost untuk development
            configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://localhost:3000"
            ));
        }
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache preflight untuk 1 jam
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Konfigurasi filter chain agar endpoint /api/auth/** bisa diakses TANPA login
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF untuk memudahkan API call
            .authorizeHttpRequests(auth -> auth
                // Izinkan semua akses ke endpoint auth dan GET events (public)
                .requestMatchers("/api/auth/**", "/api/events/**", "/api/registrations/**").permitAll()
                .anyRequest().authenticated() // Sisanya harus login (nanti dikembangkan lagi)
            );

        return http.build();
    }
}