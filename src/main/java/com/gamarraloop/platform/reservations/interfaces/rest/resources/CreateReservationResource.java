package com.gamarraloop.platform.reservations.interfaces.rest.resources;

import java.util.UUID;

public record CreateReservationResource(
    UUID lotId,
    UUID artisanId
) {}
