package com.gamarraloop.platform.lots.interfaces.rest.transform;

import com.gamarraloop.platform.lots.domain.model.aggregate.Lot;
import com.gamarraloop.platform.lots.interfaces.rest.resources.LotResource;

public class LotResourceFromEntityAssembler {

    public static LotResource toResourceFromEntity(Lot lot) {
        return new LotResource(
                lot.getId(),
                lot.getPublisherId(),
                lot.getTitle(),
                lot.getDescription(),
                lot.getTextileType(),
                lot.getWeightKg(),
                lot.getStatus().name(),
                lot.getImageUrl(),
                lot.getPickupLat(),
                lot.getPickupLng(),
                lot.getPickupRef(),
                lot.getPublishedAt() != null ? lot.getPublishedAt().toString() : null,
                lot.getCreatedAt() != null ? lot.getCreatedAt().toString() : null,
                lot.getUpdatedAt() != null ? lot.getUpdatedAt().toString() : null
        );
    }
}
