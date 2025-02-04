package io.builders.demo.surikata.application.transaction.command.createtransaction

import io.builders.demo.core.command.Command
import io.builders.demo.surikata.domain.transaction.event.TransactionCreatedEvent
import jakarta.validation.constraints.NotBlank

class CreateTransactionCommand extends Command<TransactionCreatedEvent> {

    @NotBlank
    String hash

    @NotBlank
    String eventName

    @NotBlank
    String contractAddress

}
