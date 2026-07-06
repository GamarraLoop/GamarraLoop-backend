package com.gamarraloop.platform.classification.domain.model.aggregate;

import com.gamarraloop.platform.classification.domain.model.commands.RequestClassificationCommand;
import com.gamarraloop.platform.classification.domain.model.valueobjects.ClassificationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class ClassificationRequestTest {

    private ClassificationRequest createClassificationRequest() {
        RequestClassificationCommand command =
                new RequestClassificationCommand("https://storage/images/lot-01.jpg");

        return new ClassificationRequest(command);
    }

    @Test
    void shouldCreateClassificationRequestSuccessfully() {
        RequestClassificationCommand command =
                new RequestClassificationCommand("https://storage/images/lot-01.jpg");

        ClassificationRequest request = new ClassificationRequest(command);

        Assertions.assertEquals("https://storage/images/lot-01.jpg", request.getImageUrl());
        Assertions.assertEquals(ClassificationStatus.PENDING, request.getStatus());
        Assertions.assertNull(request.getLabels());
        Assertions.assertNull(request.getFailureReason());
        Assertions.assertNotNull(request.getRequestedAt());
        Assertions.assertNull(request.getProcessedAt());
    }

    @Test
    void shouldMarkClassificationRequestAsProcessing() {
        ClassificationRequest request = createClassificationRequest();

        request.markProcessing();

        Assertions.assertEquals(ClassificationStatus.PROCESSING, request.getStatus());
    }

    @Test
    void shouldCompleteClassificationRequestSuccessfully() {
        ClassificationRequest request = createClassificationRequest();

        request.markProcessing();

        String labels =
                """
                [
                  {"label":"Cotton","score":0.98},
                  {"label":"Fabric","score":0.95}
                ]
                """;

        request.completeWithLabels(labels);

        Assertions.assertEquals(ClassificationStatus.COMPLETED, request.getStatus());
        Assertions.assertEquals(labels, request.getLabels());
        Assertions.assertNotNull(request.getProcessedAt());
    }

    @Test
    void shouldFailClassificationRequestSuccessfully() {
        ClassificationRequest request = createClassificationRequest();

        request.markProcessing();

        request.failWithReason("Google Vision API unavailable");

        Assertions.assertEquals(ClassificationStatus.FAILED, request.getStatus());
        Assertions.assertEquals("Google Vision API unavailable", request.getFailureReason());
        Assertions.assertNotNull(request.getProcessedAt());
    }

    @Test
    void shouldSetRequestedAtWhenCreatingClassificationRequest() {
        Instant beforeCreation = Instant.now();

        ClassificationRequest request = createClassificationRequest();

        Instant afterCreation = Instant.now();

        Assertions.assertTrue(
                !request.getRequestedAt().isBefore(beforeCreation)
                        && !request.getRequestedAt().isAfter(afterCreation)
        );
    }
}