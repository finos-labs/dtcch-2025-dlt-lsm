package io.builders.demo.dtcc.infrastructure.listener.lsm.updatesettlements

import io.builders.demo.core.command.CommandBus
import io.builders.demo.dtcc.application.command.updatesettlements.UpdateSettlementsCommand
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmExecutedDltEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UpdateSettlementsAdapter implements UpdateSettlementsPort {

    @Autowired
    CommandBus commandBus

    @Override
    void receive(LsmExecutedDltEvent event) {
        commandBus.executeAndWait(new UpdateSettlementsCommand(batchId: event.batchId))
    }

}
