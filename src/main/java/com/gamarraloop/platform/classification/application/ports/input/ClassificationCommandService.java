package com.gamarraloop.platform.classification.application.ports.input;

import com.gamarraloop.platform.classification.domain.model.aggregate.ClassificationRequest;
import com.gamarraloop.platform.classification.domain.model.commands.RequestClassificationCommand;

public interface ClassificationCommandService {

    ClassificationRequest requestClassification(RequestClassificationCommand command);
}
