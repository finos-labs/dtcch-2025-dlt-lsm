package io.builders.demo.dtcc.application.command.persistlsm

import io.builders.demo.core.command.Command
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmNetCalculatedEvent
import jakarta.validation.constraints.NotEmpty
import org.jetbrains.annotations.NotNull

class PersistLsmNetCommand extends Command<LsmNetCalculatedEvent> {

    @NotEmpty
    List<Integer> settlementIds

    @NotNull
    Integer batchId

}
