package io.builders.demo.dtcc.application.command.updatesettlements

import io.builders.demo.core.command.CommandHandler
import io.builders.demo.core.event.EventBus
import io.builders.demo.dtcc.domain.lsmbatch.LsmBatch
import io.builders.demo.dtcc.domain.lsmbatch.service.CheckLsmBatchExistsDomainService
import io.builders.demo.dtcc.domain.settlement.Settlement
import io.builders.demo.dtcc.domain.settlement.SettlementRepository
import io.builders.demo.dtcc.domain.settlement.SettlementStatus
import io.builders.demo.dtcc.domain.settlement.event.SettlementCreatedEvent
import io.builders.demo.dtcc.domain.settlement.event.SettlementsUpdatedEvent
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.OffsetDateTime

@Component
class UpdateSettlementsCommandHandler implements CommandHandler<SettlementsUpdatedEvent, UpdateSettlementsCommand> {

    @Autowired
    CheckLsmBatchExistsDomainService checkLsmBatchExistsDomainService

    @Autowired
    SettlementRepository settlementRepository

    @Autowired
    EventBus eventBus

    @Override
    SettlementsUpdatedEvent handle(@Valid UpdateSettlementsCommand command) {
        LsmBatch lsmBatch = checkLsmBatchExistsDomainService.execute(command.batchId)
        List<Integer> settlementIds = []
        lsmBatch.settlements.forEach {
            it.status = SettlementStatus.COMPLETED
            settlementRepository.save(it)
            settlementIds.add(it.id)
        }

        eventBus.publish(new SettlementsUpdatedEvent(
            settlementIds: settlementIds
        ))
    }

}
