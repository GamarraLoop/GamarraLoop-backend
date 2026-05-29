package com.gamarraloop.platform.lots.interfaces.rest.resources;

import java.math.BigDecimal;

public record UpdateLotResource(
        String title,
        String description,
        String textileType,
        BigDecimal weightKg,
        String imageUrl,
        BigDecimal pickupLat,
        BigDecimal pickupLng,
        String pickupRef
) {
}
