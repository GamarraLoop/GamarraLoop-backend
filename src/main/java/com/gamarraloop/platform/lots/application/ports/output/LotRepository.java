package com.gamarraloop.platform.lots.application.ports.output;

import com.gamarraloop.platform.lots.domain.model.aggregate.Lot;
import com.gamarraloop.platform.lots.domain.model.valueobjects.LotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LotRepository extends JpaRepository<Lot, UUID>, JpaSpecificationExecutor<Lot> {
    List<Lot> findByStatus(LotStatus status);
    List<Lot> findByPublisherId(UUID publisherId);
    long countByPublisherIdAndStatus(UUID publisherId, LotStatus status);
}
