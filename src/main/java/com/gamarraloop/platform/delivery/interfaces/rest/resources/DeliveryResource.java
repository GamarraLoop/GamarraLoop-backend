package com.gamarraloop.platform.delivery.interfaces.rest.resources;

import java.util.UUID;

public record DeliveryResource(
        UUID id,
        UUID reservationId,
        String status,
        String startedAt,
        String completedAt,
        String createdAt,
        String updatedAt
) {
}
