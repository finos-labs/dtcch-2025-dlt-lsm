package io.builders.demo.dtcc.application.command.updatesettlements

import io.builders.demo.core.command.Command
import io.builders.demo.dtcc.domain.settlement.event.SettlementsUpdatedEvent
import jakarta.validation.constraints.NotNull

class UpdateSettlementsCommand extends Command<SettlementsUpdatedEvent> {

    @NotNull
    Integer batchId

}
