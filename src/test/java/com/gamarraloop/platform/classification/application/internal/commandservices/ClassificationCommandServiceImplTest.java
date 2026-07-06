package com.gamarraloop.platform.classification.application.internal.commandservices;

import com.gamarraloop.platform.classification.application.ports.output.ClassificationRequestRepository;
import com.gamarraloop.platform.classification.application.ports.output.VisionApiPort;
import com.gamarraloop.platform.classification.domain.model.aggregate.ClassificationRequest;
import com.gamarraloop.platform.classification.domain.model.commands.RequestClassificationCommand;
import com.gamarraloop.platform.classification.domain.model.valueobjects.ClassificationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassificationCommandServiceImplTest {

    @Mock
    private ClassificationRequestRepository classificationRequestRepository;

    @Mock
    private VisionApiPort visionApiPort;

    @InjectMocks
    private ClassificationCommandServiceImpl classificationCommandService;

    @Test
    void shouldRequestClassificationSuccessfully() {
        RequestClassificationCommand command =
                new RequestClassificationCommand("https://storage/images/lot-01.jpg");

        when(visionApiPort.analyzeImage(command.imageUrl()))
                .thenReturn("""
                        [
                          {"label":"Cotton","score":0.98}
                        ]
                        """);

        when(classificationRequestRepository.save(ArgumentMatchers.any(ClassificationRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ClassificationRequest result =
                classificationCommandService.requestClassification(command);

        Assertions.assertEquals(ClassificationStatus.COMPLETED, result.getStatus());
        Assertions.assertNotNull(result.getLabels());
        Assertions.assertNull(result.getFailureReason());

        verify(visionApiPort).analyzeImage(command.imageUrl());
        verify(classificationRequestRepository, times(3))
                .save(any(ClassificationRequest.class));
    }

    @Test
    void shouldFailClassificationWhenVisionApiThrowsException() {
        RequestClassificationCommand command =
                new RequestClassificationCommand("https://storage/images/lot-01.jpg");

        when(visionApiPort.analyzeImage(command.imageUrl()))
                .thenThrow(new RuntimeException("Vision API unavailable"));

        when(classificationRequestRepository.save(any(ClassificationRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ClassificationRequest result =
                classificationCommandService.requestClassification(command);

        Assertions.assertEquals(ClassificationStatus.FAILED, result.getStatus());
        Assertions.assertEquals("Vision API unavailable", result.getFailureReason());

        verify(visionApiPort).analyzeImage(command.imageUrl());
        verify(classificationRequestRepository, times(3))
                .save(any(ClassificationRequest.class));
    }
}