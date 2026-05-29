package com.gamarraloop.platform.lots.application.internal.commandservices;

import com.gamarraloop.platform.lots.application.ports.input.LotCommandService;
import com.gamarraloop.platform.lots.application.ports.output.LotRepository;
import com.gamarraloop.platform.lots.domain.model.aggregate.Lot;
import com.gamarraloop.platform.lots.domain.model.commands.CreateLotCommand;
import com.gamarraloop.platform.lots.domain.model.commands.UpdateLotCommand;
import com.gamarraloop.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class LotCommandServiceImpl implements LotCommandService {

    private final LotRepository lotRepository;

    public LotCommandServiceImpl(LotRepository lotRepository) {
        this.lotRepository = lotRepository;
    }

    @Override
    public Lot create(CreateLotCommand command) {
        var lot = new Lot(command);
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
}
