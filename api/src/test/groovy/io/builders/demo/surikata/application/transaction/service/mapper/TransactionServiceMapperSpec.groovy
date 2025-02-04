package io.builders.demo.surikata.application.transaction.service.mapper

import io.builders.demo.core.BaseSpecification
import io.builders.demo.core.event.DltEvent
import io.builders.demo.core.event.EventListener
import io.builders.demo.surikata.domain.transaction.Transaction
import io.builders.demo.surikata.domain.transaction.TransactionTestFactory
import io.builders.demo.surikata.infrastructure.configuration.SurikataTransactionMapperConfiguration
import net.consensys.eventeum.dto.transaction.TransactionDetails
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.Instant

class TransactionServiceMapperSpec extends BaseSpecification {

    private static class TokenTransferredFailedDltEvent extends DltEvent { }

    private static Optional<TokenTransferredFailedDltEvent> expectedEvent = Optional.empty()

    @Component
    private static class TestListener implements EventListener<TokenTransferredFailedDltEvent> {

        @Override
        void receive(TokenTransferredFailedDltEvent event) {
            expectedEvent = Optional.of(event)
        }

    }

    @SpringBean
    SurikataTransactionMapperConfiguration configuration = Mock()

    @Autowired
    TransactionServiceMapper mapper

    void 'should publish a domain event, when the contract event is given'() {
        given:
        configuration.mappers >> ['TokenTransferOrdered': TokenTransferredFailedDltEvent]
        Transaction transaction = TransactionTestFactory.create { Transaction transaction ->
            transaction.eventName = configuration.mappers.firstEntry().key
        }

        TransactionDetails event = new TransactionDetails(
            contractAddress: transaction.contractAddress,
            blockTimestamp: Instant.now().epochSecond,
            hash: transaction.hash,
        )

        when:
        mapper.execute(event)

        then:
        Awaitility.await().atMost(Durations.FIVE_SECONDS).until {
            expectedEvent.present
        }
        expectedEvent.get().with { ev ->
            verifyAll {
                ev.transactionHash == event.transactionHash
                ev.contractAddress == event.contractAddress
            }
        }
    }

}
