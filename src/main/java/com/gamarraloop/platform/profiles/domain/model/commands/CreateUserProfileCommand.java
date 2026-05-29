package com.gamarraloop.platform.profiles.domain.model.commands;

public record CreateUserProfileCommand(
        String fullName,
        String email,
        String phone,
        String role,
        String deviceId
) {
}
