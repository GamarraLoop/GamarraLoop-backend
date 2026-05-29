package com.gamarraloop.platform.notifications.interfaces.rest.resources;

import java.util.UUID;

public record CreateNotificationResource(UUID userId, String title, String message) {
}
