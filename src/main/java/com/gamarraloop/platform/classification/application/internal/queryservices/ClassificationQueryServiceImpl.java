package com.gamarraloop.platform.classification.application.internal.queryservices;

import com.gamarraloop.platform.classification.application.ports.input.ClassificationQueryService;
import com.gamarraloop.platform.classification.application.ports.output.ClassificationRequestRepository;
import com.gamarraloop.platform.classification.domain.model.aggregate.ClassificationRequest;
import com.gamarraloop.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClassificationQueryServiceImpl implements ClassificationQueryService {

    private final ClassificationRequestRepository classificationRequestRepository;

    public ClassificationQueryServiceImpl(ClassificationRequestRepository classificationRequestRepository) {
        this.classificationRequestRepository = classificationRequestRepository;
    }

    @Override
    public ClassificationRequest getByLotId(UUID lotId) {
        return classificationRequestRepository.findByLotId(lotId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassificationRequest", lotId));
    }

    @Override
    public ClassificationRequest getById(UUID id) {
        return classificationRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassificationRequest", id));
    }
}
