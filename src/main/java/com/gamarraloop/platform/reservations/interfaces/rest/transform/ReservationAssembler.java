package com.gamarraloop.platform.reservations.interfaces.rest.transform;

import com.gamarraloop.platform.reservations.domain.model.aggregate.Reservation;
import com.gamarraloop.platform.reservations.domain.model.commands.CreateReservationCommand;
import com.gamarraloop.platform.reservations.interfaces.rest.resources.CreateReservationResource;
import com.gamarraloop.platform.reservations.interfaces.rest.resources.ReservationResource;

public class ReservationAssembler {

    private ReservationAssembler() {
        // Utility class
    }

    public static CreateReservationCommand toCommandFromResource(CreateReservationResource resource) {
        return new CreateReservationCommand(
                resource.lotId(),
                resource.artisanId()
        );
    }

    public static ReservationResource toResourceFromEntity(Reservation entity) {
        return new ReservationResource(
                entity.getId(),
                entity.getLotId(),
                entity.getArtisanId(),
                entity.getStatus().name(),
                entity.getReservedAt() != null ? entity.getReservedAt().toString() : null,
                entity.getExpiresAt() != null ? entity.getExpiresAt().toString() : null,
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
                entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null
        );
    }
}
