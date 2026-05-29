package com.gamarraloop.platform.reservations.domain.model.commands;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateReservationCommand(
    @NotNull UUID lotId,
    @NotNull UUID artisanId
) {}
