package io.builders.demo.dtcc.application.command.starlsmbatch

import io.builders.demo.core.command.Command
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmBatchStartedEvent
import io.builders.demo.dtcc.domain.settlement.event.SettlementCreatedEvent
import io.builders.demo.dtcc.domain.user.User
import jakarta.validation.constraints.NotNull

class StartLsmBatchCommand extends Command<LsmBatchStartedEvent>{


}
