package com.haibui.inpogram.models.dtos;

import com.haibui.inpogram.models.enums.AuthProvider;

public record UserDTO(
        int id,
        String username,
        String email,
        AuthProvider provider,
        String providerId,
        String profileImageUrl,
        String role
) {
}
