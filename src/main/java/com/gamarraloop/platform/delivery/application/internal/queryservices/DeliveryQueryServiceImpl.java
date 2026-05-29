package com.gamarraloop.platform.delivery.application.internal.queryservices;

import com.gamarraloop.platform.delivery.application.ports.input.DeliveryQueryService;
import com.gamarraloop.platform.delivery.domain.model.aggregate.DeliveryProcess;
import com.gamarraloop.platform.delivery.infrastructure.persistence.jpa.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeliveryQueryServiceImpl implements DeliveryQueryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryQueryServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryProcess> getById(UUID id) {
        return deliveryRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryProcess> getByReservationId(UUID reservationId) {
        return deliveryRepository.findByReservationId(reservationId);
    }
}
