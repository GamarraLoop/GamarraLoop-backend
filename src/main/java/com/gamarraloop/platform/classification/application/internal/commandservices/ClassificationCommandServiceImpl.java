package com.gamarraloop.platform.classification.application.internal.commandservices;

import com.gamarraloop.platform.classification.application.ports.input.ClassificationCommandService;
import com.gamarraloop.platform.classification.application.ports.output.ClassificationRequestRepository;
import com.gamarraloop.platform.classification.application.ports.output.VisionApiPort;
import com.gamarraloop.platform.classification.domain.model.aggregate.ClassificationRequest;
import com.gamarraloop.platform.classification.domain.model.commands.RequestClassificationCommand;
import org.springframework.stereotype.Service;

@Service
public class ClassificationCommandServiceImpl implements ClassificationCommandService {

    private final ClassificationRequestRepository classificationRequestRepository;
    private final VisionApiPort visionApiPort;

    public ClassificationCommandServiceImpl(ClassificationRequestRepository classificationRequestRepository,
                                            VisionApiPort visionApiPort) {
        this.classificationRequestRepository = classificationRequestRepository;
        this.visionApiPort = visionApiPort;
    }

    @Override
    public ClassificationRequest requestClassification(RequestClassificationCommand command) {
        var request = new ClassificationRequest(command);
        classificationRequestRepository.save(request);

        request.markProcessing();
        classificationRequestRepository.save(request);

        try {
            String result = visionApiPort.analyzeImage(command.imageUrl());
            request.completeWithLabels(result);
        } catch (Exception e) {
            request.failWithReason(e.getMessage());
        }

        return classificationRequestRepository.save(request);
    }
}
