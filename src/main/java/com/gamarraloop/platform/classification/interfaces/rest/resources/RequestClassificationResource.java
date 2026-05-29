package com.gamarraloop.platform.classification.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RequestClassificationResource(
        @NotNull(message = "Lot ID is required")
        UUID lotId,

        @NotBlank(message = "Image URL is required")
        String imageUrl
) {
}
