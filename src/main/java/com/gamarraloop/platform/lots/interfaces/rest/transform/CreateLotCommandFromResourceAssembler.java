package com.gamarraloop.platform.lots.interfaces.rest.transform;

import com.gamarraloop.platform.lots.domain.model.commands.CreateLotCommand;
import com.gamarraloop.platform.lots.interfaces.rest.resources.CreateLotResource;

public class CreateLotCommandFromResourceAssembler {

    public static CreateLotCommand toCommandFromResource(CreateLotResource resource) {
        return new CreateLotCommand(
                resource.publisherId(),
                resource.title(),
                resource.description(),
                resource.textileType(),
                resource.weightKg(),
                resource.imageUrl(),
                resource.pickupLat(),
                resource.pickupLng(),
                resource.pickupRef()
        );
    }
}
