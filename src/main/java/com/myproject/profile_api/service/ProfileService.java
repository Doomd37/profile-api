package com.myproject.profile_api.service;

import com.myproject.profile_api.dto.ProfileResponse;

import java.util.List;

public interface ProfileService {

    ProfileResponse createProfile(String name);

    ProfileResponse getProfileById(String id);

    List<ProfileResponse> getAllProfiles(String gender, String countryId, String ageGroup);

    void deleteProfile(String id);
}
