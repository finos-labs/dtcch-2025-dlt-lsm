package io.builders.demo.surikata.application.transaction.command.updatetransactionstatus

import io.builders.demo.core.command.Command
import io.builders.demo.surikata.domain.transaction.event.TransactionStatusUpdatedEvent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class UpdateTransactionStatusCommand extends Command<TransactionStatusUpdatedEvent> {

    @NotNull
    UUID id

    @NotBlank
    String status

}
