package com.gamarraloop.platform.lots.application.internal.commandservices;

import com.gamarraloop.platform.lots.application.ports.input.LotCommandService;
import com.gamarraloop.platform.lots.application.ports.output.LotRepository;
import com.gamarraloop.platform.lots.domain.model.aggregate.Lot;
import com.gamarraloop.platform.lots.domain.model.commands.CreateLotCommand;
import com.gamarraloop.platform.lots.domain.model.commands.UpdateLotCommand;
import com.gamarraloop.platform.classification.application.ports.input.TextileInferenceService;
import com.gamarraloop.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class LotCommandServiceImpl implements LotCommandService {

    private static final Logger logger = LoggerFactory.getLogger(LotCommandServiceImpl.class);

    private final LotRepository lotRepository;
    private final TextileInferenceService textileInferenceService;

    public LotCommandServiceImpl(LotRepository lotRepository,
                                 TextileInferenceService textileInferenceService) {
        this.lotRepository = lotRepository;
        this.textileInferenceService = textileInferenceService;
    }

    @Override
    public Lot create(CreateLotCommand command) {
        var lot = new Lot(command);
        // Clasificación IA automática: si hay imagen, infiere el tipo de textil y
        // sobrescribe el valor manual. Si Vision falla, se conserva el manual.
        try {
            String inferred = textileInferenceService.inferTextileType(command.imageUrl());
            lot.applyInferredTextileType(inferred);
        } catch (Exception e) {
            logger.warn("Auto-clasificación IA falló; se conserva el tipo manual del lote: {}", e.getMessage());
        }
        return lotRepository.save(lot);
    }

    @Override
    public Lot update(UUID id, UpdateLotCommand command) {
        var lot = lotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot", id));
        lot.updateDetails(command);
        return lotRepository.save(lot);
    }

    @Override
    public Lot publish(UUID id) {
        var lot = lotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot", id));
        lot.publish();
        return lotRepository.save(lot);
    }

    @Override
    public Lot withdraw(UUID id) {
        var lot = lotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot", id));
        lot.withdraw();
        return lotRepository.save(lot);
    }

    @Override
    public Lot markReserved(UUID id) {
        var lot = lotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot", id));
        lot.markReserved();
        return lotRepository.save(lot);
    }

    @Override
    public Lot release(UUID id) {
        var lot = lotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot", id));
        lot.release();
        return lotRepository.save(lot);
    }

    @Override
    public Lot markPickedUp(UUID id) {
        var lot = lotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot", id));
        lot.markPickedUp();
        return lotRepository.save(lot);
    }

    @Override
    public void delete(UUID id, UUID publisherId) {
        var lot = lotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot", id));
        if (publisherId == null || !lot.getPublisherId().equals(publisherId)) {
            throw new IllegalArgumentException("Solo el confeccionista dueño puede eliminar el lote");
        }
        var status = lot.getStatus();
        if (status == com.gamarraloop.platform.lots.domain.model.valueobjects.LotStatus.RESERVED
                || status == com.gamarraloop.platform.lots.domain.model.valueobjects.LotStatus.PICKED_UP) {
            throw new IllegalStateException("No se puede eliminar un lote reservado o entregado");
        }
        lotRepository.delete(lot);
    }
}
