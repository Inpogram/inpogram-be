package com.haibui.inpogram.models.repositories;

import com.haibui.inpogram.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByProviderId(String providerId);
}
