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
    String updatedAt,
    // Datos del lote asociado (join), para que la app muestre algo legible en vez del UUID.
    String lotTitle,
    String lotTextileType,
    String lotPickupAddress
) {}
