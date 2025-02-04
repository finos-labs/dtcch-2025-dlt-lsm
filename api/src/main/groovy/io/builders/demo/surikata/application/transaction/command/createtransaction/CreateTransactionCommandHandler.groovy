package io.builders.demo.surikata.application.transaction.command.createtransaction

import io.builders.demo.core.command.CommandHandler
import io.builders.demo.core.event.EventBus
import io.builders.demo.surikata.domain.transaction.Transaction
import io.builders.demo.surikata.domain.transaction.TransactionRepository
import io.builders.demo.surikata.domain.transaction.event.TransactionCreatedEvent
import io.builders.demo.surikata.domain.transaction.service.CheckTransactionAlreadyExistsDomainService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CreateTransactionCommandHandler implements CommandHandler<TransactionCreatedEvent, CreateTransactionCommand> {

    @Autowired
    EventBus eventBus

    @Autowired
    TransactionRepository repository

    @Autowired
    CheckTransactionAlreadyExistsDomainService checkTransactionAlreadyExistsDomainService

    @Override
    TransactionCreatedEvent handle(@Valid CreateTransactionCommand command) {
        checkTransactionAlreadyExistsDomainService.execute(command.hash)

        Transaction transaction = repository.save(new Transaction(
            hash: command.hash,
            eventName: command.eventName,
            contractAddress: command.contractAddress,
        ))

        eventBus.publish(new TransactionCreatedEvent(
            id: transaction.id,
            hash: transaction.hash,
            contractAddress: transaction.contractAddress
        ))
    }

}
