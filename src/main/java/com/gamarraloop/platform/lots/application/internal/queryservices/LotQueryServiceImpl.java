package com.gamarraloop.platform.lots.application.internal.queryservices;

import com.gamarraloop.platform.lots.application.ports.input.LotQueryService;
import com.gamarraloop.platform.lots.application.ports.output.LotRepository;
import com.gamarraloop.platform.lots.domain.model.aggregate.Lot;
import com.gamarraloop.platform.lots.domain.model.valueobjects.LotStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
