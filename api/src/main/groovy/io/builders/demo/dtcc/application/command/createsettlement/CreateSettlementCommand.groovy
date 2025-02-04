package io.builders.demo.dtcc.application.command.createsettlement

import io.builders.demo.core.command.Command
import io.builders.demo.dtcc.domain.settlement.event.SettlementCreatedEvent
import io.builders.demo.dtcc.domain.user.User
import jakarta.validation.constraints.NotNull

class CreateSettlementCommand extends Command<SettlementCreatedEvent> {

    @NotNull
    User buyer

    @NotNull
    User seller

    @NotNull
    BigDecimal securityAmount

    @NotNull
    BigDecimal cashAmount

}
