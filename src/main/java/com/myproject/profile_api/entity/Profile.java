package com.myproject.profile_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "profiles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id
    private String id;

    @Column(unique = true)
    private String name;

    private String gender;
    private Double genderProbability;
    private Integer sampleSize;

    private Integer age;
    private String ageGroup;

    private String countryId;
    private Double countryProbability;

    private Instant createdAt;
}