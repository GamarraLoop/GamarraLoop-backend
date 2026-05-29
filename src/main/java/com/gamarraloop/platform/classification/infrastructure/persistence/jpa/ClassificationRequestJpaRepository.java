package com.gamarraloop.platform.classification.infrastructure.persistence.jpa;

import com.gamarraloop.platform.classification.application.ports.output.ClassificationRequestRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassificationRequestJpaRepository extends ClassificationRequestRepository {
}
