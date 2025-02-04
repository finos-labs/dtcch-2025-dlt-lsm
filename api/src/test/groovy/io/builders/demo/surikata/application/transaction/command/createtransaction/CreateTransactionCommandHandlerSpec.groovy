package io.builders.demo.surikata.application.transaction.command.createtransaction

import io.builders.demo.core.BaseSpecification
import io.builders.demo.core.RandomGenerator
import io.builders.demo.core.command.CommandBus
import io.builders.demo.core.domain.exception.UnsupportedOperationDomainException
import io.builders.demo.surikata.domain.transaction.Transaction
import io.builders.demo.surikata.domain.transaction.TransactionTestFactory
import io.builders.demo.surikata.domain.transaction.event.TransactionCreatedEvent
import io.builders.demo.surikata.infrastructure.listener.transaction.TransactionTestListener
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.springframework.beans.factory.annotation.Autowired

class CreateTransactionCommandHandlerSpec extends BaseSpecification {

    @Autowired
    CommandBus commandBus

    void 'should emit an event, when the command is executed successfully'() {
        given:
        CreateTransactionCommand command = new CreateTransactionCommand(
            hash: RandomGenerator.transactionHash(),
            contractAddress: RandomGenerator.address(),
            eventName: RandomGenerator.eventName()
        )

        when:
        commandBus.executeAndWait(command)

        then:
        Awaitility.await().atMost(Durations.FIVE_SECONDS).until {
            TransactionTestListener.TRANSACTION_CREATED_EVENTS.size() > 0
        }
        TransactionCreatedEvent event = TransactionTestListener.TRANSACTION_CREATED_EVENTS.first
        verifyAll {
            event.contractAddress == command.contractAddress
            event.hash == command.hash
            event.id
        }
    }

    void 'should throw an exception, when the command is executed and the transaction already exists'() {
        given:
        Transaction transaction = TransactionTestFactory.create()

        when:
        commandBus.executeAndWait(new CreateTransactionCommand(
            hash: transaction.hash,
            eventName: transaction.eventName,
            contractAddress: transaction.contractAddress
        ))

        then:
        thrown(UnsupportedOperationDomainException)
    }

}
