package com.gamarraloop.platform.delivery.application.internal.commandservices;

import com.gamarraloop.platform.delivery.application.ports.input.DeliveryCommandService;
import com.gamarraloop.platform.delivery.domain.model.aggregate.DeliveryProcess;
import com.gamarraloop.platform.delivery.domain.model.commands.StartDeliveryCommand;
import com.gamarraloop.platform.delivery.infrastructure.persistence.jpa.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeliveryCommandServiceImpl implements DeliveryCommandService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryCommandServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    @Transactional
    public DeliveryProcess startDelivery(StartDeliveryCommand command) {
        DeliveryProcess deliveryProcess = new DeliveryProcess(command);
        return deliveryRepository.save(deliveryProcess);
    }

    @Override
    @Transactional
    public DeliveryProcess completeDelivery(UUID id) {
        DeliveryProcess deliveryProcess = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryProcess not found for id: " + id));
        deliveryProcess.complete();
        return deliveryRepository.save(deliveryProcess);
    }

    @Override
    @Transactional
    public DeliveryProcess failDelivery(UUID id) {
        DeliveryProcess deliveryProcess = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryProcess not found for id: " + id));
        deliveryProcess.fail();
        return deliveryRepository.save(deliveryProcess);
    }
}
