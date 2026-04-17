package com.myproject.profile_api.controller;

import com.myproject.profile_api.dto.ApiResponse;
import com.myproject.profile_api.dto.ProfileRequest;
import com.myproject.profile_api.dto.ProfileResponse;
import com.myproject.profile_api.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> create(@RequestBody ProfileRequest req) {

        ApiResponse<ProfileResponse> response = service.createProfile(req.getName());

        if (response.getMessage() != null &&
                response.getMessage().equals("Profile already exists")) {
            return ResponseEntity.ok(response); // 200
        }

        return ResponseEntity.status(201).body(response); // 201
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfileResponse>> get(@PathVariable String id) {

        return ResponseEntity.ok(
                new ApiResponse<>("success", null, service.getProfileById(id))
        );
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProfileResponse>>> all(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String country_id,
            @RequestParam(required = false) String age_group
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>("success", null,
                        service.getAllProfiles(gender, country_id, age_group))
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        service.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}