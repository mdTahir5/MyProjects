package com.phonebook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Public, unauthenticated endpoint useful for smoke tests and for the frontend
 * to verify that it is pointed at a live API.
 *
 * <p>
 * Deliberately exposes no configuration or build details.
 * </p>
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "phonebook-backend",
                "timestamp", Instant.now()));
    }
}
