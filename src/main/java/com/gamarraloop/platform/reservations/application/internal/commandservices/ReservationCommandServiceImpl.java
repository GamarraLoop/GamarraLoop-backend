package com.gamarraloop.platform.reservations.application.internal.commandservices;

import com.gamarraloop.platform.reservations.application.ports.input.ReservationCommandService;
import com.gamarraloop.platform.reservations.domain.model.aggregate.Reservation;
import com.gamarraloop.platform.reservations.domain.model.commands.CreateReservationCommand;
import com.gamarraloop.platform.reservations.domain.model.valueobjects.ReservationStatus;
import com.gamarraloop.platform.reservations.infrastructure.persistence.jpa.ReservationRepository;
import com.gamarraloop.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private final ReservationRepository reservationRepository;

    public ReservationCommandServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
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
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation complete(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        reservation.complete();
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation cancel(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        reservation.cancel();
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation expire(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        reservation.expire();
        return reservationRepository.save(reservation);
    }
}
