package com.gamarraloop.platform.classification.domain.model.commands;

import java.util.UUID;

public record RequestClassificationCommand(UUID lotId, String imageUrl) {
}
