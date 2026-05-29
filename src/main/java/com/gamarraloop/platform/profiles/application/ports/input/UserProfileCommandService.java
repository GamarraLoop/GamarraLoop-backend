package com.gamarraloop.platform.profiles.application.ports.input;

import com.gamarraloop.platform.profiles.domain.model.aggregate.UserProfile;
import com.gamarraloop.platform.profiles.domain.model.commands.CreateUserProfileCommand;
import com.gamarraloop.platform.profiles.domain.model.commands.UpdateUserProfileCommand;

import java.util.UUID;

public interface UserProfileCommandService {

    UserProfile create(CreateUserProfileCommand command);

    UserProfile update(UUID id, UpdateUserProfileCommand command);
}
