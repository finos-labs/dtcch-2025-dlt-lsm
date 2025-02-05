package io.builders.demo.dtcc.application.command.persistlsm

import io.builders.demo.core.command.CommandHandler
import io.builders.demo.core.event.EventBus
import io.builders.demo.dtcc.domain.lsmbatch.LsmBatch
import io.builders.demo.dtcc.domain.lsmbatch.LsmBatchRepository
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmNetCalculatedEvent
import io.builders.demo.dtcc.domain.lsmbatch.service.CheckLsmBatchExistsDomainService
import io.builders.demo.dtcc.domain.settlement.Settlement
import io.builders.demo.dtcc.domain.settlement.SettlementRepository
import io.builders.demo.dtcc.domain.settlement.SettlementStatus
import io.builders.demo.dtcc.domain.settlement.event.SettlementCreatedEvent
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.OffsetDateTime

@Component
class PersistLsmNetCommandHandler implements CommandHandler<LsmNetCalculatedEvent, PersistLsmNetCommand> {

    @Autowired
    CheckLsmBatchExistsDomainService checkLsmBatchExistsDomainService

    @Autowired
    LsmBatchRepository lsmBatchRepository

    @Autowired
    SettlementRepository settlementRepository

    @Autowired
    EventBus eventBus

    @Override
    LsmNetCalculatedEvent handle(@Valid PersistLsmNetCommand command) {
        LsmBatch batch = checkLsmBatchExistsDomainService.execute(command.batchId)

        List<Settlement> filteredSettlements = batch.settlements.findAll { command.settlementIds.contains(it.id) }
        List<Settlement> oldSettlements = batch.settlements

        List<Settlement> discardedSettlements = (oldSettlements - filteredSettlements)
        discardedSettlements.each { Settlement settlement ->
            settlement.status = SettlementStatus.PENDING
            settlement.lsmBatch = null
        }
        settlementRepository.saveAll(discardedSettlements)

        batch.settlements = filteredSettlements
        batch.aiResult = command.aiOutput
        lsmBatchRepository.save(batch)

        eventBus.publish(new LsmNetCalculatedEvent(
            lsmId: command.batchId,
        ))
    }

}
