package com.gamarraloop.platform.delivery.domain.model.commands;

import java.util.UUID;

public record StartDeliveryCommand(UUID reservationId) {
}
