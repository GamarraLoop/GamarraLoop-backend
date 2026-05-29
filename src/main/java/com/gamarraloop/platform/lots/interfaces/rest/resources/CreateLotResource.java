package com.gamarraloop.platform.lots.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateLotResource(
        @NotNull UUID publisherId,
        @NotBlank String title,
        String description,
        String textileType,
        BigDecimal weightKg,
        String imageUrl,
        BigDecimal pickupLat,
        BigDecimal pickupLng,
        String pickupRef
) {
}
