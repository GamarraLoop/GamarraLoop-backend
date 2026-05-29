package com.gamarraloop.platform.notifications.interfaces.rest.transform;

import com.gamarraloop.platform.notifications.domain.model.commands.CreateNotificationCommand;
import com.gamarraloop.platform.notifications.interfaces.rest.resources.CreateNotificationResource;

public class CreateNotificationCommandFromResourceAssembler {
    public static CreateNotificationCommand toCommandFromResource(CreateNotificationResource resource) {
        return new CreateNotificationCommand(resource.userId(), resource.title(), resource.message());
    }
}
