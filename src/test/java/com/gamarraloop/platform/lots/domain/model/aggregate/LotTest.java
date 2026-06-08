package com.gamarraloop.platform.lots.domain.model.aggregate;

import com.gamarraloop.platform.lots.domain.model.commands.CreateLotCommand;
import com.gamarraloop.platform.lots.domain.model.commands.UpdateLotCommand;
import com.gamarraloop.platform.lots.domain.model.valueobjects.LotStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LotTest {

    private Lot createLot() {
        CreateLotCommand command =
                new CreateLotCommand(
                        UUID.randomUUID(),
                        "Cotton Scraps",
                        "Reusable textile remnants",
                        "Cotton",
                        BigDecimal.valueOf(10),
                        "image.jpg",
                        BigDecimal.valueOf(-12.0464),
                        BigDecimal.valueOf(-77.0428),
                        "Stand 15"
                );
        return new Lot(command);
    }

    @Test
    void shouldCreateLotInDraftStatus() {
        //Arrange
        UUID publisherId = UUID.randomUUID();
        CreateLotCommand createLotCommand = new CreateLotCommand(publisherId,
                "Cotton Scraps",
                "Reusable textile remnants",
                "Cotton",
                BigDecimal.valueOf(10),
                "image.jpg",
                BigDecimal.valueOf(-12.0464),
                BigDecimal.valueOf(-77.0428),
                "Stand 15");
        //Act
        Lot lot = new Lot(createLotCommand);
        //Assert
        assertEquals(LotStatus.DRAFT, lot.getStatus());
    }

    @Test
    void shouldPublishLotWhenStatusIsDraft(){
        //Arrange
        Lot lot = createLot();
        //Act
        lot.publish();
        //Assert
        assertEquals(LotStatus.PUBLISHED, lot.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenPublishingAlreadyPublishedLot(){
        //Arrange
        Lot lot = createLot();
        lot.publish();
        //Act
        //Assert
        assertThrows(IllegalStateException.class, lot::publish);
    }

    @Test
    void shouldWithdrawPublishedLot(){
        //Arrange
        Lot lot = createLot();
        lot.publish();
        //Act
        lot.withdraw();
        //Assert
        assertEquals(LotStatus.WITHDRAWN, lot.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenWithdrawingDraftLot() {
        //Arrange
        Lot lot = createLot();
        //Act
        //Assert
        assertThrows(
                IllegalStateException.class,
                lot::withdraw
        );
    }

    @Test
    void shouldReservePublishedLot(){
        //Arrange
        Lot lot = createLot();
        lot.publish();
        //Act
        lot.markReserved();
        //Assert
        assertEquals(LotStatus.RESERVED, lot.getStatus());
    }

    @Test
    void shouldUpdateLotDetailsWhenDraft() {
        //Arrange
        Lot lot = createLot();
        UpdateLotCommand command =
                new UpdateLotCommand(
                        "New Title",
                        "New Description",
                        "Polyester",
                        BigDecimal.valueOf(20),
                        "new-image.jpg",
                        BigDecimal.ONE,
                        BigDecimal.ONE,
                        "New Ref"
                );
        //Act
        lot.updateDetails(command);
        //Assert
        assertEquals("New Title", lot.getTitle());
        assertEquals("Polyester", lot.getTextileType());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPublishedLot() {
        //Arrange
        Lot lot = createLot();
        lot.publish();
        UpdateLotCommand command =
                new UpdateLotCommand(
                        "New Title",
                        "Description",
                        "Polyester",
                        BigDecimal.TEN,
                        "image.jpg",
                        BigDecimal.ONE,
                        BigDecimal.ONE,
                        "Ref"
                );
        //Act
        //Assert
        assertThrows(
                IllegalStateException.class,
                () -> lot.updateDetails(command)
        );
    }

}