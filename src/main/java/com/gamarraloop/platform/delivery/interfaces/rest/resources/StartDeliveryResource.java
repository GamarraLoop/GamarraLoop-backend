package com.gamarraloop.platform.delivery.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record StartDeliveryResource(@NotNull UUID reservationId) {
}
