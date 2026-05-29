package com.gamarraloop.platform.profiles.interfaces.rest.resources;

public record UpdateUserProfileResource(
        String fullName,
        String email,
        String phone
) {
}
