package com.haibui.inpogram.services;

import com.haibui.inpogram.models.dtos.UserDTO;
import com.haibui.inpogram.models.entities.User;
import com.haibui.inpogram.models.repositories.UserRepository;
import com.haibui.inpogram.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO getUserData(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String email = principal.getEmail();
        String providerId = principal.getProviderId();

        User user = providerId != null
                ? userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new RuntimeException("User not found"))
                : userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO(
                (int) user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProvider(),
                user.getProviderId(),
                user.getProfileImageUrl(),
                user.getRole().toString()
        );
    }
}
