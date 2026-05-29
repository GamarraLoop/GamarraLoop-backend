package com.gamarraloop.platform.classification.application.ports.output;

import com.gamarraloop.platform.classification.domain.model.aggregate.ClassificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClassificationRequestRepository extends JpaRepository<ClassificationRequest, UUID> {

    Optional<ClassificationRequest> findByLotId(UUID lotId);
}
