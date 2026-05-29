package com.gamarraloop.platform.delivery.interfaces.rest.transform;

import com.gamarraloop.platform.delivery.domain.model.commands.StartDeliveryCommand;
import com.gamarraloop.platform.delivery.interfaces.rest.resources.StartDeliveryResource;

public class StartDeliveryCommandFromResourceAssembler {
    public static StartDeliveryCommand toCommandFromResource(StartDeliveryResource resource) {
        return new StartDeliveryCommand(resource.reservationId());
    }
}
