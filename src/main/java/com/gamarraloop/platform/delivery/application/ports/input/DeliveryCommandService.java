package com.gamarraloop.platform.delivery.application.ports.input;

import com.gamarraloop.platform.delivery.domain.model.aggregate.DeliveryProcess;
import com.gamarraloop.platform.delivery.domain.model.commands.StartDeliveryCommand;

import java.util.UUID;

public interface DeliveryCommandService {
    DeliveryProcess startDelivery(StartDeliveryCommand command);
    DeliveryProcess completeDelivery(UUID id);
    DeliveryProcess failDelivery(UUID id);
}
