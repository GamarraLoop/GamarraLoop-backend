package com.gamarraloop.platform.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateUserProfileResource(
        @NotBlank String fullName,
        String email,
        @NotBlank String phone,
        @NotBlank String role,
        String deviceId
) {
}
