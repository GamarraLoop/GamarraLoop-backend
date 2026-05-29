package com.gamarraloop.platform.lots.application.ports.input;

import com.gamarraloop.platform.lots.domain.model.aggregate.Lot;
import com.gamarraloop.platform.lots.domain.model.commands.CreateLotCommand;
import com.gamarraloop.platform.lots.domain.model.commands.UpdateLotCommand;

import java.util.UUID;

public interface LotCommandService {
    Lot create(CreateLotCommand command);
    Lot update(UUID id, UpdateLotCommand command);
    Lot publish(UUID id);
    Lot withdraw(UUID id);
}
