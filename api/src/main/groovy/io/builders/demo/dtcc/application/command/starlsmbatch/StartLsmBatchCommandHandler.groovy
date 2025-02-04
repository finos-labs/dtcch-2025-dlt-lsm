package io.builders.demo.dtcc.application.command.starlsmbatch

import io.builders.demo.core.command.CommandHandler
import io.builders.demo.core.event.EventBus
import io.builders.demo.dtcc.domain.lsmbatch.LsmBatch
import io.builders.demo.dtcc.domain.lsmbatch.LsmBatchRepository
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmBatchStartedEvent
import io.builders.demo.dtcc.domain.settlement.Settlement
import io.builders.demo.dtcc.domain.settlement.SettlementRepository
import io.builders.demo.dtcc.domain.settlement.SettlementStatus
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class StartLsmBatchCommandHandler implements CommandHandler<LsmBatchStartedEvent, StartLsmBatchCommand> {

    @Autowired
    LsmBatchRepository lsmBatchRepository

    @Autowired
    SettlementRepository settlementRepository

    @Autowired
    EventBus eventBus

    @Override
    LsmBatchStartedEvent handle(@Valid StartLsmBatchCommand command) {
        LsmBatch lsmBatch = this.lsmBatchRepository.save(new LsmBatch())
        List<Settlement> settlementPending = settlementRepository.findAllByStatusIs(SettlementStatus.PENDING)
        settlementPending.forEach {
            it.setStatus(SettlementStatus.PROCESSING)
            it.setLsmBatch(lsmBatch)
            lsmBatch.addSettlement(it)
        }
        lsmBatch = lsmBatchRepository.save(lsmBatch)
        eventBus.publish(new LsmBatchStartedEvent(
            id: lsmBatch.id,
            settlements: settlementPending*.id
        ))
    }

}
