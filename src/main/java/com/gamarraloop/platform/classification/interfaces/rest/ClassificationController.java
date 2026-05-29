package com.gamarraloop.platform.classification.interfaces.rest;

import com.gamarraloop.platform.classification.application.ports.input.ClassificationCommandService;
import com.gamarraloop.platform.classification.application.ports.input.ClassificationQueryService;
import com.gamarraloop.platform.classification.domain.model.aggregate.ClassificationRequest;
import com.gamarraloop.platform.classification.interfaces.rest.resources.ClassificationResource;
import com.gamarraloop.platform.classification.interfaces.rest.resources.RequestClassificationResource;
import com.gamarraloop.platform.classification.interfaces.rest.transform.ClassificationResourceFromEntityAssembler;
import com.gamarraloop.platform.classification.interfaces.rest.transform.RequestClassificationCommandFromResourceAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/classifications")
public class ClassificationController {

    private final ClassificationCommandService classificationCommandService;
    private final ClassificationQueryService classificationQueryService;

    public ClassificationController(ClassificationCommandService classificationCommandService,
                                    ClassificationQueryService classificationQueryService) {
        this.classificationCommandService = classificationCommandService;
        this.classificationQueryService = classificationQueryService;
    }

    @PostMapping
    public ResponseEntity<ClassificationResource> requestClassification(
            @Valid @RequestBody RequestClassificationResource resource) {
        var command = RequestClassificationCommandFromResourceAssembler.toCommandFromResource(resource);
        ClassificationRequest result = classificationCommandService.requestClassification(command);
        ClassificationResource response = ClassificationResourceFromEntityAssembler.toResourceFromEntity(result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassificationResource> getById(@PathVariable UUID id) {
        ClassificationRequest result = classificationQueryService.getById(id);
        ClassificationResource response = ClassificationResourceFromEntityAssembler.toResourceFromEntity(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lot/{lotId}")
    public ResponseEntity<ClassificationResource> getByLotId(@PathVariable UUID lotId) {
        ClassificationRequest result = classificationQueryService.getByLotId(lotId);
        ClassificationResource response = ClassificationResourceFromEntityAssembler.toResourceFromEntity(result);
        return ResponseEntity.ok(response);
    }
}
