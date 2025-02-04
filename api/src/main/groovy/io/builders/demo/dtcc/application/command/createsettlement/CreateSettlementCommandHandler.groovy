package io.builders.demo.dtcc.application.command.createsettlement

import io.builders.demo.dtcc.domain.settlement.Settlement
import io.builders.demo.dtcc.domain.settlement.SettlementRepository
import io.builders.demo.dtcc.domain.settlement.event.SettlementCreatedEvent
import io.builders.demo.core.command.CommandHandler
import io.builders.demo.core.event.EventBus
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired

import java.time.OffsetDateTime

class CreateSettlementCommandHandler implements CommandHandler<SettlementCreatedEvent, CreateSettlementCommand> {

    @Autowired
    SettlementRepository settlementRepository

    @Autowired
    EventBus eventBus

    @Override
    SettlementCreatedEvent handle(@Valid CreateSettlementCommand command) {
        Settlement settlement = this.settlementRepository.save(new Settlement(
            seller: command.seller,
            buyer: command.buyer,
            cashAmount: command.cashAmount,
            securityAmount: command.securityAmount,
            executionDate: OffsetDateTime.now()
        ))

        eventBus.publish(new SettlementCreatedEvent(
            id: settlement.id,
            sellerId: settlement.seller.id,
            buyerId: settlement.buyer.id,
            cashAmount: settlement.cashAmount,
            securityAmount: settlement.securityAmount,
        ))

    }
}
