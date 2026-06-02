package com.gamarraloop.platform.reservations.application.internal.queryservices;

import com.gamarraloop.platform.reservations.application.ports.input.ReservationQueryService;
import com.gamarraloop.platform.reservations.domain.model.aggregate.Reservation;
import com.gamarraloop.platform.reservations.domain.model.valueobjects.ReservationStatus;
import com.gamarraloop.platform.reservations.infrastructure.persistence.jpa.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationRepository reservationRepository;

    public ReservationQueryServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> getById(UUID id) {
        return reservationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getByLotId(UUID lotId) {
        return reservationRepository.findByLotId(lotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getByArtisanId(UUID artisanId) {
        return reservationRepository.findByArtisanId(artisanId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getExpiredActiveReservations() {
        return reservationRepository.findByStatusAndExpiresAtBefore(ReservationStatus.ACTIVE, Instant.now());
    }
}
