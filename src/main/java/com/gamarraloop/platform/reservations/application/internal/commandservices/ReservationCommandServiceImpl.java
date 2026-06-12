package com.gamarraloop.platform.reservations.application.internal.commandservices;

import com.gamarraloop.platform.reservations.application.ports.input.ReservationCommandService;
import com.gamarraloop.platform.reservations.domain.model.aggregate.Reservation;
import com.gamarraloop.platform.reservations.domain.model.commands.CreateReservationCommand;
import com.gamarraloop.platform.reservations.domain.model.valueobjects.ReservationStatus;
import com.gamarraloop.platform.lots.application.ports.input.LotCommandService;
import com.gamarraloop.platform.reservations.infrastructure.persistence.jpa.ReservationRepository;
import com.gamarraloop.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private final ReservationRepository reservationRepository;
    private final LotCommandService lotCommandService;

    public ReservationCommandServiceImpl(ReservationRepository reservationRepository,
                                         LotCommandService lotCommandService) {
        this.reservationRepository = reservationRepository;
        this.lotCommandService = lotCommandService;
    }

    @Override
    @Transactional
    public Reservation create(CreateReservationCommand command) {
        // Check for existing active reservation on this lot
        boolean lotAlreadyReserved = reservationRepository.findByLotId(command.lotId()).stream()
                .anyMatch(r -> r.getStatus() == ReservationStatus.ACTIVE);
        if (lotAlreadyReserved) {
            throw new IllegalStateException("Lot " + command.lotId() + " already has an active reservation.");
        }

        Reservation reservation = new Reservation(command);
        Reservation saved = reservationRepository.save(reservation);

        // Transición del lote PUBLISHED -> RESERVED dentro de la misma transacción.
        // Si el lote no está PUBLISHED, markReserved lanza y revierte la reserva completa.
        lotCommandService.markReserved(command.lotId());

        return saved;
    }

    @Override
    @Transactional
    public Reservation complete(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        reservation.complete();
        Reservation saved = reservationRepository.save(reservation);
        // El artesano confirmó la recepción física: el lote queda PICKED_UP.
        lotCommandService.markPickedUp(reservation.getLotId());
        return saved;
    }

    @Override
    @Transactional
    public Reservation cancel(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        reservation.cancel();
        Reservation saved = reservationRepository.save(reservation);
        // Reserva cancelada: el lote vuelve a estar disponible.
        lotCommandService.release(reservation.getLotId());
        return saved;
    }

    @Override
    @Transactional
    public Reservation expire(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        reservation.expire();
        Reservation saved = reservationRepository.save(reservation);
        // Reserva expirada: el lote vuelve a estar disponible.
        lotCommandService.release(reservation.getLotId());
        return saved;
    }
}
