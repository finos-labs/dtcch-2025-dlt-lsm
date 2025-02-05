package io.builders.demo.dtcc.application.command.createsettlement

import io.builders.demo.core.command.CommandHandler
import io.builders.demo.core.event.EventBus
import io.builders.demo.dtcc.domain.settlement.Settlement
import io.builders.demo.dtcc.domain.settlement.SettlementRepository
import io.builders.demo.dtcc.domain.settlement.event.SettlementCreatedEvent
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.OffsetDateTime

@Component
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

        Optional<Settlement> result = settlementRepository.findById(settlement.id)

        eventBus.publish(new SettlementCreatedEvent(
            id: settlement.id,
            sellerId: settlement.seller.id,
            buyerId: settlement.buyer.id,
            cashAmount: settlement.cashAmount,
            securityAmount: settlement.securityAmount,
        ))
    }

}
