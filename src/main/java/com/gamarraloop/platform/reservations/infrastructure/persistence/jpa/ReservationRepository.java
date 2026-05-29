package com.gamarraloop.platform.reservations.infrastructure.persistence.jpa;

import com.gamarraloop.platform.reservations.domain.model.aggregate.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByLotId(UUID lotId);
    List<Reservation> findByArtisanId(UUID artisanId);
}
