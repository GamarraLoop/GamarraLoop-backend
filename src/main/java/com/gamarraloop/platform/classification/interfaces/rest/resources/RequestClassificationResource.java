package com.gamarraloop.platform.classification.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record RequestClassificationResource(
        @NotBlank(message = "Image URL is required")
        String imageUrl
) {
}
