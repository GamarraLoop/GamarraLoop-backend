package com.gamarraloop.platform.lots.application.ports.input;

import com.gamarraloop.platform.lots.domain.model.aggregate.Lot;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LotQueryService {
    Optional<Lot> getById(UUID id);
    List<Lot> getAll();
    List<Lot> getByStatus(String status);
    List<Lot> getByPublisherId(UUID publisherId);

    /** Lista lotes aplicando filtros opcionales (cualquiera puede ser null). */
    List<Lot> search(String status, UUID publisherId, String textileType);

    /** Conteo de lotes de un publicador, agrupado por estado. */
    long countByPublisherAndStatus(UUID publisherId, String status);
}
