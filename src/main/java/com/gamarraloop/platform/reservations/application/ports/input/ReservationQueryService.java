package com.gamarraloop.platform.reservations.application.ports.input;

import com.gamarraloop.platform.reservations.domain.model.aggregate.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationQueryService {
    Optional<Reservation> getById(UUID id);
    List<Reservation> getByLotId(UUID lotId);
    List<Reservation> getByArtisanId(UUID artisanId);
    List<Reservation> getExpiredActiveReservations();
}
