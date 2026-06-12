package com.gamarraloop.platform.profiles.interfaces.rest.transform;

import com.gamarraloop.platform.profiles.domain.model.commands.CreateUserProfileCommand;
import com.gamarraloop.platform.profiles.interfaces.rest.resources.CreateUserProfileResource;

public class CreateUserProfileCommandFromResourceAssembler {

    public static CreateUserProfileCommand toCommandFromResource(CreateUserProfileResource resource) {
        return new CreateUserProfileCommand(
                resource.id(),
                resource.fullName(),
                resource.email(),
                resource.phone(),
                resource.role(),
                resource.deviceId()
        );
    }
}
