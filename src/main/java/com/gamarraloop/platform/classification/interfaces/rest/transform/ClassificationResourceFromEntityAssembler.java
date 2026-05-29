package com.gamarraloop.platform.classification.interfaces.rest.transform;

import com.gamarraloop.platform.classification.domain.model.aggregate.ClassificationRequest;
import com.gamarraloop.platform.classification.interfaces.rest.resources.ClassificationResource;

public class ClassificationResourceFromEntityAssembler {

    public static ClassificationResource toResourceFromEntity(ClassificationRequest entity) {
        return new ClassificationResource(
                entity.getId(),
                entity.getLotId(),
                entity.getImageUrl(),
                entity.getStatus().name(),
                entity.getLabels(),
                entity.getFailureReason(),
                entity.getRequestedAt() != null ? entity.getRequestedAt().toString() : null,
                entity.getProcessedAt() != null ? entity.getProcessedAt().toString() : null
        );
    }
}
