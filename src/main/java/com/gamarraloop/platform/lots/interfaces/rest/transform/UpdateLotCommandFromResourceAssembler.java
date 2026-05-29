package com.gamarraloop.platform.lots.interfaces.rest.transform;

import com.gamarraloop.platform.lots.domain.model.commands.UpdateLotCommand;
import com.gamarraloop.platform.lots.interfaces.rest.resources.UpdateLotResource;

public class UpdateLotCommandFromResourceAssembler {

    public static UpdateLotCommand toCommandFromResource(UpdateLotResource resource) {
        return new UpdateLotCommand(
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
