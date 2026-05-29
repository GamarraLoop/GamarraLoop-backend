package com.gamarraloop.platform.reservations.application.ports.input;

import com.gamarraloop.platform.reservations.domain.model.aggregate.Reservation;
import com.gamarraloop.platform.reservations.domain.model.commands.CreateReservationCommand;

import java.util.UUID;

public interface ReservationCommandService {
    Reservation create(CreateReservationCommand command);
    Reservation complete(UUID id);
    Reservation cancel(UUID id);
    Reservation expire(UUID id);
}
