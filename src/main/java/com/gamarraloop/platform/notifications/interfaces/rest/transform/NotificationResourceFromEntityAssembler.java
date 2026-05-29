package com.gamarraloop.platform.notifications.interfaces.rest.transform;

import com.gamarraloop.platform.notifications.domain.model.aggregate.Notification;
import com.gamarraloop.platform.notifications.interfaces.rest.resources.NotificationResource;

public class NotificationResourceFromEntityAssembler {
    public static NotificationResource toResourceFromEntity(Notification entity) {
        String createdAt = entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null;
        String updatedAt = entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null;
        return new NotificationResource(
                entity.getId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getIsRead(),
                createdAt,
                updatedAt
        );
    }
}
