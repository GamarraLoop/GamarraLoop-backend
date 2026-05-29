package com.gamarraloop.platform.lots.domain.model.commands;

import java.math.BigDecimal;

public record UpdateLotCommand(
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
