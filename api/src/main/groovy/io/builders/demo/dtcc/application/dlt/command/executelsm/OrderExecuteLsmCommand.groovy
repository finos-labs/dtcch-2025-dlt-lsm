package io.builders.demo.dtcc.application.dlt.command.executelsm

import io.builders.demo.dtcc.application.dlt.common.Transaction
import io.builders.demo.dtcc.domain.lsmbatch.event.ExecuteLsmOrderedEvent
import io.builders.demo.core.command.Command

class OrderExecuteLsmCommand extends Command<ExecuteLsmOrderedEvent> {

    List<Transaction> transactions

}
