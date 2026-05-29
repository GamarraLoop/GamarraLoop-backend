package com.gamarraloop.platform.delivery.interfaces.rest.transform;

import com.gamarraloop.platform.delivery.domain.model.aggregate.DeliveryProcess;
import com.gamarraloop.platform.delivery.interfaces.rest.resources.DeliveryResource;

public class DeliveryResourceFromEntityAssembler {
    public static DeliveryResource toResourceFromEntity(DeliveryProcess entity) {
        return new DeliveryResource(
                entity.getId(),
                entity.getReservationId(),
                entity.getStatus().name(),
                entity.getStartedAt() != null ? entity.getStartedAt().toString() : null,
                entity.getCompletedAt() != null ? entity.getCompletedAt().toString() : null,
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
                entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null
        );
    }
}
