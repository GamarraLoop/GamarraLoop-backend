package com.gamarraloop.platform.notifications.application.ports.input;

import com.gamarraloop.platform.notifications.domain.model.aggregate.Notification;
import com.gamarraloop.platform.notifications.domain.model.commands.CreateNotificationCommand;

import java.util.Optional;
import java.util.UUID;

public interface NotificationCommandService {
    Optional<Notification> create(CreateNotificationCommand command);
    Optional<Notification> markAsRead(UUID id);
}
