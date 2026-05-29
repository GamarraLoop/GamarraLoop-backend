package com.gamarraloop.platform.lots.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.UUID;

public record LotResource(
        UUID id,
        UUID publisherId,
        String title,
        String description,
        String textileType,
        BigDecimal weightKg,
        String status,
        String imageUrl,
        BigDecimal pickupLat,
        BigDecimal pickupLng,
        String pickupRef,
        String publishedAt,
        String createdAt,
        String updatedAt
) {
}
