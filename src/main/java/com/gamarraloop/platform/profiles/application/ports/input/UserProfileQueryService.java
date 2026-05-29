package com.gamarraloop.platform.profiles.application.ports.input;

import com.gamarraloop.platform.profiles.domain.model.aggregate.UserProfile;

import java.util.List;
import java.util.UUID;

public interface UserProfileQueryService {

    UserProfile getById(UUID id);

    List<UserProfile> getAll();

    List<UserProfile> getByRole(String role);
}
