package com.gamarraloop.platform.classification.interfaces.rest.resources;

import java.util.UUID;

public record ClassificationResource(
        UUID id,
        UUID lotId,
        String imageUrl,
        String status,
        String labels,
        String failureReason,
        String requestedAt,
        String processedAt
) {
}
