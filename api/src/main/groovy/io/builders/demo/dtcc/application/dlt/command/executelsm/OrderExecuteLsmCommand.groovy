package io.builders.demo.dtcc.application.dlt.command.executelsm

import io.builders.demo.core.command.Command
import io.builders.demo.dtcc.application.dlt.common.Transaction
import io.builders.demo.dtcc.domain.lsmbatch.event.ExecuteLsmOrderedEvent

class OrderExecuteLsmCommand extends Command<ExecuteLsmOrderedEvent> {

    Integer batchId
    List<Transaction> transactions

}
