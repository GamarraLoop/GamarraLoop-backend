package com.gamarraloop.platform.profiles.domain.model.commands;

public record UpdateUserProfileCommand(
        String fullName,
        String email,
        String phone
) {
}
