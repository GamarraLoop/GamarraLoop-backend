package com.gamarraloop.platform.profiles.domain.model.commands;

public record CreateUserProfileCommand(
        String id,
        String fullName,
        String email,
        String phone,
        String role,
        String deviceId
) {
}
