package com.gamarraloop.platform.delivery.infrastructure.persistence.jpa;

import com.gamarraloop.platform.delivery.domain.model.aggregate.DeliveryProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryProcess, UUID> {
    List<DeliveryProcess> findByReservationId(UUID reservationId);
}
