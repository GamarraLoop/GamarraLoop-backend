package com.gamarraloop.platform.classification.application.ports.input;

import com.gamarraloop.platform.classification.domain.model.aggregate.ClassificationRequest;

import java.util.UUID;

public interface ClassificationQueryService {

    ClassificationRequest getByLotId(UUID lotId);

    ClassificationRequest getById(UUID id);
}
