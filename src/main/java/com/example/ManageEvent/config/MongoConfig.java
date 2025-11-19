package com.example.ManageEvent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing // Ini penting agar @CreatedDate otomatis terisi
public class MongoConfig {
}