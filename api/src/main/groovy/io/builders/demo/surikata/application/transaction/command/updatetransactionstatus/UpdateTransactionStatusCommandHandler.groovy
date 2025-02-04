package io.builders.demo.surikata.application.transaction.command.updatetransactionstatus

import io.builders.demo.core.command.CommandHandler
import io.builders.demo.core.event.EventBus
import io.builders.demo.surikata.domain.transaction.Transaction
import io.builders.demo.surikata.domain.transaction.TransactionRepository
import io.builders.demo.surikata.domain.transaction.TransactionStatus
import io.builders.demo.surikata.domain.transaction.event.TransactionStatusUpdatedEvent
import io.builders.demo.surikata.domain.transaction.service.CheckTransactionExistsDomainService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UpdateTransactionStatusCommandHandler
    implements CommandHandler<TransactionStatusUpdatedEvent, UpdateTransactionStatusCommand> {

    @Autowired
    CheckTransactionExistsDomainService checkTransactionExistsDomainService

    @Autowired
    TransactionRepository repository

    @Autowired
    EventBus eventBus

    @Override
    TransactionStatusUpdatedEvent handle(@Valid UpdateTransactionStatusCommand command) {
//        Transaction transaction = checkTransactionExistsDomainService.execute(command.id)
//        TransactionStatus currentStatus = transaction.status
//
//        transaction.status = command.status as TransactionStatus
//        repository.save(transaction)
//
//        eventBus.publish(new TransactionStatusUpdatedEvent(
//            id: transaction.id,
//            previousStatus: currentStatus,
//            newStatus: transaction.status
//        ))
    }

}
