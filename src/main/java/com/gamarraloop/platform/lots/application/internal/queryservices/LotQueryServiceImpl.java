package com.gamarraloop.platform.lots.application.internal.queryservices;

import com.gamarraloop.platform.lots.application.ports.input.LotQueryService;
import com.gamarraloop.platform.lots.application.ports.output.LotRepository;
import com.gamarraloop.platform.lots.domain.model.aggregate.Lot;
import com.gamarraloop.platform.lots.domain.model.valueobjects.LotStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class LotQueryServiceImpl implements LotQueryService {

    private final LotRepository lotRepository;

    public LotQueryServiceImpl(LotRepository lotRepository) {
        this.lotRepository = lotRepository;
    }

    @Override
    public Optional<Lot> getById(UUID id) {
        return lotRepository.findById(id);
    }

    @Override
    public List<Lot> getAll() {
        return lotRepository.findAll();
    }

    @Override
    public List<Lot> getByStatus(String status) {
        var lotStatus = LotStatus.valueOf(status.toUpperCase());
        return lotRepository.findByStatus(lotStatus);
    }

    @Override
    public List<Lot> getByPublisherId(UUID publisherId) {
        return lotRepository.findByPublisherId(publisherId);
    }

    @Override
    public List<Lot> search(String status, UUID publisherId, String textileType) {
        Specification<Lot> spec = (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            if (status != null && !status.isBlank()) {
                preds.add(cb.equal(root.get("status"), LotStatus.valueOf(status.toUpperCase())));
            }
            if (publisherId != null) {
                preds.add(cb.equal(root.get("publisherId"), publisherId));
            }
            if (textileType != null && !textileType.isBlank()) {
                // Insensible a mayúsculas/minúsculas: las etiquetas en la app
                // son "Algodón" pero la BD puede tener "ALGODON" o "Algodón".
                preds.add(cb.equal(cb.lower(root.get("textileType")), textileType.toLowerCase()));
            }
            return cb.and(preds.toArray(new Predicate[0]));
        };
        return lotRepository.findAll(spec);
    }

    @Override
    public long countByPublisherAndStatus(UUID publisherId, String status) {
        return lotRepository.countByPublisherIdAndStatus(
                publisherId, LotStatus.valueOf(status.toUpperCase()));
    }
}
