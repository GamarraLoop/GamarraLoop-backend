package com.gamarraloop.platform.notifications.application.ports.input;

import com.gamarraloop.platform.notifications.domain.model.aggregate.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationQueryService {
    Optional<Notification> getById(UUID id);
    List<Notification> getByUserId(UUID userId);
    List<Notification> getUnreadByUserId(UUID userId);
}
