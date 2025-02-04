package io.builders.demo.surikata.infrastructure.listener.transaction.createtransaction

import io.builders.demo.core.BaseSpecification
import io.builders.demo.core.RandomGenerator
import io.builders.demo.core.command.CommandBus
import io.builders.demo.core.event.EventBus
import io.builders.demo.core.event.TransactionEvent
import io.builders.demo.surikata.application.transaction.command.createtransaction.CreateTransactionCommand
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CreateTransactionAdapterSpec extends BaseSpecification {

    private static class DummyEvent extends TransactionEvent { }

    @Autowired
    EventBus eventBus

    @SpringBean
    CommandBus commandBus = Mock()

    void 'should call the correct command, when the event is published'() {
        given:
        String transactionId = RandomGenerator.transactionHash()
        String contractAddress = RandomGenerator.address()
        DummyEvent event = new DummyEvent(transactionId: transactionId, contractAddress: contractAddress)

        CountDownLatch latch = new CountDownLatch(1)

        commandBus.executeAndWait(_ as CreateTransactionCommand) >> {
            latch.countDown()
        }

        when:
        eventBus.publish(event)

        latch.await(2, TimeUnit.SECONDS)

        then:
        1 * commandBus.executeAndWait(_ as CreateTransactionCommand)
    }

}
