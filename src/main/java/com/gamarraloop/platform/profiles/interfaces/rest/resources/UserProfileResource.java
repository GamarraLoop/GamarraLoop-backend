package com.gamarraloop.platform.profiles.interfaces.rest.resources;

import java.util.UUID;

public record UserProfileResource(
        UUID id,
        String fullName,
        String email,
        String phone,
        String role,
        String deviceId,
        String createdAt,
        String updatedAt
) {
}
