package com.myproject.profile_api.service.serviceImpl;

import com.myproject.profile_api.Exception.BadRequestException;
import com.myproject.profile_api.Exception.ExternalApiException;
import com.myproject.profile_api.Exception.NotFoundException;
import com.myproject.profile_api.dto.*;
import com.myproject.profile_api.entity.Profile;
import com.myproject.profile_api.repository.ProfileRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import com.myproject.profile_api.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;
    private final WebClient webClient;

    // ================= CREATE PROFILE =================
    @Override
    public ProfileResponse createProfile(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Missing or empty name");
        }

        final String normalizedName = name.toLowerCase().trim();

        return repository.findByName(normalizedName)
                .map(this::map)
                .orElseGet(() -> createNewProfile(normalizedName));
    }

    private ProfileResponse createNewProfile(String name) {

        GenderResponse gender = getGender(name);
        AgifyResponse age = getAge(name);
        NationalizeResponse nat = getNationality(name);

        // EDGE CASES (502 RULES)
        if (gender == null || gender.getGender() == null || gender.getCount() == 0) {
            throw new ExternalApiException("Genderize");
        }

        if (age == null || age.getAge() == null) {
            throw new ExternalApiException("Agify");
        }

        if (nat == null || nat.getCountry() == null || nat.getCountry().isEmpty()) {
            throw new ExternalApiException("Nationalize");
        }

        String ageGroup = computeAgeGroup(age.getAge());

        CountryData topCountry = nat.getCountry()
                .stream()
                .max(Comparator.comparing(CountryData::getProbability))
                .orElseThrow();

        Profile profile = Profile.builder()
                .id(UuidCreator.getTimeOrderedEpoch().toString())
                .name(name)
                .gender(gender.getGender())
                .genderProbability(gender.getProbability())
                .sampleSize(gender.getCount())
                .age(age.getAge())
                .ageGroup(ageGroup)
                .countryId(topCountry.getCountry_id())
                .countryProbability(topCountry.getProbability())
                .createdAt(Instant.now())
                .build();

        repository.save(profile);

        return map(profile);
    }

    // ================= GET BY ID =================
    @Override
    public ProfileResponse getProfileById(String id) {

        Profile profile = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profile not found"));

        return map(profile);
    }

    // ================= GET ALL + FILTER =================
    @Override
    public List<ProfileResponse> getAllProfiles(String gender, String countryId, String ageGroup) {

        return repository.findAll().stream()
                .filter(p -> gender == null || p.getGender().equalsIgnoreCase(gender))
                .filter(p -> countryId == null || p.getCountryId().equalsIgnoreCase(countryId))
                .filter(p -> ageGroup == null || p.getAgeGroup().equalsIgnoreCase(ageGroup))
                .map(this::map)
                .toList();
    }

    // ================= DELETE =================
    @Override
    public void deleteProfile(String id) {

        Profile profile = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profile not found"));

        repository.delete(profile);
    }

    // ================= AGE GROUP LOGIC =================
    private String computeAgeGroup(int age) {
        if (age <= 12) return "child";
        if (age <= 19) return "teenager";
        if (age <= 59) return "adult";
        return "senior";
    }

    // ================= EXTERNAL APIs =================
    private GenderResponse getGender(String name) {
        return webClient.get()
                .uri("https://api.genderize.io?name=" + name)
                .retrieve()
                .bodyToMono(GenderResponse.class)
                .block();
    }

    private AgifyResponse getAge(String name) {
        return webClient.get()
                .uri("https://api.agify.io?name=" + name)
                .retrieve()
                .bodyToMono(AgifyResponse.class)
                .block();
    }

    private NationalizeResponse getNationality(String name) {
        return webClient.get()
                .uri("https://api.nationalize.io?name=" + name)
                .retrieve()
                .bodyToMono(NationalizeResponse.class)
                .block();
    }

    // ================= MAPPER =================
    private ProfileResponse map(Profile p) {
        return ProfileResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .gender(p.getGender())
                .gender_probability(p.getGenderProbability())
                .sample_size(p.getSampleSize())
                .age(p.getAge())
                .age_group(p.getAgeGroup())
                .country_id(p.getCountryId())
                .country_probability(p.getCountryProbability())
                .created_at(p.getCreatedAt())
                .build();
    }
}