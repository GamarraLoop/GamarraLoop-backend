package com.gamarraloop.platform.lots.domain.model.commands;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateLotCommand(
        UUID publisherId,
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
