package io.builders.demo.surikata.application.event.service.map

import io.builders.demo.core.BaseSpecification
import io.builders.demo.core.RandomGenerator
import io.builders.demo.core.event.DltEvent
import io.builders.demo.core.event.EventListener
import io.builders.demo.surikata.domain.transaction.Transaction
import io.builders.demo.surikata.domain.transaction.TransactionTestFactory
import io.builders.demo.surikata.infrastructure.configuration.SurikataEventMapperConfiguration
import net.consensys.eventeum.dto.event.ContractEventDetails
import net.consensys.eventeum.dto.event.parameter.NumberParameter
import net.consensys.eventeum.dto.event.parameter.StringParameter
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.modelmapper.AbstractConverter
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class ContractEventServiceMapperSpec extends BaseSpecification {

    private static class TokenTransferredDltEvent extends DltEvent {

        BigInteger amount
        String address

    }
    private static Optional<TokenTransferredDltEvent> expectedEvent = Optional.empty()

    @Component
    private static class TestListener implements EventListener<TokenTransferredDltEvent> {

        @Override
        void receive(TokenTransferredDltEvent event) {
            expectedEvent = Optional.of(event)
        }

    }

    @Component
    private static class TestConverter extends AbstractConverter<ContractEventDetails, TokenTransferredDltEvent> {

        @Override
        protected TokenTransferredDltEvent convert(ContractEventDetails event) {
            return new TokenTransferredDltEvent(
                amount: (event.nonIndexedParameters[0] as NumberParameter).value,
                address: (event.nonIndexedParameters[1] as StringParameter).value,
                contractAddress: event.address,
                transactionHash: event.transactionHash,
                blockTimestamp: OffsetDateTime.ofInstant(Instant.ofEpochSecond(event.blockTimestamp.toLong()), ZoneId.systemDefault())
            )
        }

    }

    @SpringBean
    SurikataEventMapperConfiguration configuration = Mock()

    @Autowired
    ContractEventServiceMapper mapper

    void 'should publish a domain event, when the contract event is given'() {
        given:
        Transaction transaction = TransactionTestFactory.create()
        String address = RandomGenerator.address()
        BigInteger amount = BigInteger.ONE

        configuration.mappers >> [TokenTransferred: TokenTransferredDltEvent]

        ContractEventDetails event = new ContractEventDetails(
            address: transaction.contractAddress,
            transactionHash: transaction.hash,
            name: configuration.mappers.keySet().first(),
            blockTimestamp: Instant.now().epochSecond,
            nonIndexedParameters: [
                new NumberParameter('uint256', amount),
                new StringParameter('address', address)
            ]
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
                ev.contractAddress == event.address
                ev.address == address
                ev.amount == amount
            }
        }
    }

}
