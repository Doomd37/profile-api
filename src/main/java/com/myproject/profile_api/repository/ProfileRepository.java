package com.myproject.profile_api.repository;

import com.myproject.profile_api.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findByName(String name);
}
