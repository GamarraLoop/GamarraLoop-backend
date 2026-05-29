package com.gamarraloop.platform.reservations.interfaces.rest.resources;

import java.util.UUID;

public record ReservationResource(
    UUID id,
    UUID lotId,
    UUID artisanId,
    String status,
    String reservedAt,
    String expiresAt,
    String createdAt,
    String updatedAt
) {}
