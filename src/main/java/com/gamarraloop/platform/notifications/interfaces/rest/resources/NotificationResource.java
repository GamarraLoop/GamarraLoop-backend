package com.gamarraloop.platform.notifications.interfaces.rest.resources;

import java.util.UUID;

public record NotificationResource(UUID id, UUID userId, String title, String message, Boolean isRead, String createdAt, String updatedAt) {
}
