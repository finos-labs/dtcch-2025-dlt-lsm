package io.builders.demo.surikata.infrastructure.listener.transaction.createtransaction

import io.builders.demo.core.command.CommandBus
import io.builders.demo.core.event.TransactionEvent
import io.builders.demo.surikata.application.transaction.command.createtransaction.CreateTransactionCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CreateTransactionAdapter implements CreateTransactionPort {

    @Autowired
    CommandBus commandBus

    @Override
    void receive(TransactionEvent event) {
        commandBus.executeAndWait(new CreateTransactionCommand(
            hash: event.transactionId,
            eventName: event.eventType,
            contractAddress: event.contractAddress
        ))
    }

}
