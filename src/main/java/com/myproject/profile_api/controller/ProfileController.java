package com.myproject.profile_api.controller;

import com.myproject.profile_api.dto.ApiResponse;
import com.myproject.profile_api.dto.ProfileRequest;
import com.myproject.profile_api.dto.ProfileResponse;
import com.myproject.profile_api.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> create(@Valid @RequestBody ProfileRequest req) {

        ApiResponse<ProfileResponse> response = service.createProfile(req.getName());

        boolean exists =
                response.getMessage() != null &&
                        response.getMessage().equalsIgnoreCase("Profile already exists");

        if (exists) {
            return ResponseEntity.ok(response); // 200
        }

        return ResponseEntity.status(201).body(response); // 201
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfileResponse>> get(@PathVariable String id) {

        return ResponseEntity.ok(
                new ApiResponse<>("success",service.getProfileById(id))
        );
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProfileResponse>>> all(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String country_id,
            @RequestParam(required = false) String age_group
    ) {

        List<ProfileResponse> profiles =
                service.getAllProfiles(gender, country_id, age_group);

        return ResponseEntity.ok(
                ApiResponse.<List<ProfileResponse>>builder()
                        .status("success")
                        .count(profiles.size())
                        .data(profiles)
                        .build()
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        service.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}