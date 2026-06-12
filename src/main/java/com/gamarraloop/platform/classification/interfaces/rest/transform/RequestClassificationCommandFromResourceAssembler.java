package com.gamarraloop.platform.classification.interfaces.rest.transform;

import com.gamarraloop.platform.classification.domain.model.commands.RequestClassificationCommand;
import com.gamarraloop.platform.classification.interfaces.rest.resources.RequestClassificationResource;

public class RequestClassificationCommandFromResourceAssembler {

    public static RequestClassificationCommand toCommandFromResource(RequestClassificationResource resource) {
        return new RequestClassificationCommand(
                resource.imageUrl()
        );
    }
}

