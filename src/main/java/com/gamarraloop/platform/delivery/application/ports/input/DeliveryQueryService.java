package com.gamarraloop.platform.delivery.application.ports.input;

import com.gamarraloop.platform.delivery.domain.model.aggregate.DeliveryProcess;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryQueryService {
    Optional<DeliveryProcess> getById(UUID id);
    List<DeliveryProcess> getByReservationId(UUID reservationId);
}
