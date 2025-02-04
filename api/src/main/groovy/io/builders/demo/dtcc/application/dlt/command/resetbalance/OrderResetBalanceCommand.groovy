package io.builders.demo.dtcc.application.dlt.command.resetbalance

import io.builders.demo.core.command.Command
import io.builders.demo.dtcc.domain.lsmbatch.event.ResetBalancesOrderedEvent

class OrderResetBalanceCommand extends Command<ResetBalancesOrderedEvent> {

    List<String> addresses
    BigDecimal amount

}
