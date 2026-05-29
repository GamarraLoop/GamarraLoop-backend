package com.gamarraloop.platform.profiles.interfaces.rest.transform;

import com.gamarraloop.platform.profiles.domain.model.aggregate.UserProfile;
import com.gamarraloop.platform.profiles.interfaces.rest.resources.UserProfileResource;

public class UserProfileResourceFromEntityAssembler {

    public static UserProfileResource toResourceFromEntity(UserProfile entity) {
        return new UserProfileResource(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getRole().name(),
                entity.getDeviceId(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
                entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null
        );
    }
}
