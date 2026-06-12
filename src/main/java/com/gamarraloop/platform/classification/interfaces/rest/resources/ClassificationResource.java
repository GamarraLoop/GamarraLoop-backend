package com.gamarraloop.platform.classification.interfaces.rest.resources;

import java.util.UUID;

public record ClassificationResource(
        UUID id,
        String imageUrl,
        String status,
        String aiDescription,
        String aiTextileType,
        // Confianza de la etiqueta principal de Vision (0.0 a 1.0).
        double aiConfidence
) {
}
