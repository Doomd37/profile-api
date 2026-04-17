package com.myproject.profile_api.controller;

import com.myproject.profile_api.dto.ProfileRequest;
import com.myproject.profile_api.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProfileRequest req) {
        return ResponseEntity.status(201).body(
                Map.of("status", "success", "data", service.createProfile(req.getName()))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return ResponseEntity.ok(
                Map.of("status", "success", "data", service.getProfileById(id))
        );
    }

    @GetMapping
    public ResponseEntity<?> all(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String country_id,
            @RequestParam(required = false) String age_group
    ) {
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "data", service.getAllProfiles(gender, country_id, age_group)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
