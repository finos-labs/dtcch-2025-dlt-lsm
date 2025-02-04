package io.builders.demo.surikata.application.transaction.service.mapper

import io.builders.demo.core.command.CommandBus
import io.builders.demo.core.event.DltEvent
import io.builders.demo.core.event.EventBus
import io.builders.demo.core.event.FailedDltEvent
import io.builders.demo.surikata.application.transaction.command.updatetransactionstatus.UpdateTransactionStatusCommand
import io.builders.demo.surikata.domain.transaction.Transaction
import io.builders.demo.surikata.domain.transaction.TransactionStatus
import io.builders.demo.surikata.domain.transaction.service.CheckTransactionExistsDomainService
import io.builders.demo.surikata.infrastructure.configuration.SurikataTransactionMapperConfiguration
import jakarta.validation.Valid
import net.consensys.eventeum.dto.transaction.TransactionDetails
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

@Component
@Validated
class TransactionServiceMapper {

    @Autowired
    CheckTransactionExistsDomainService checkTransactionExistsDomainService

    @Autowired
    SurikataTransactionMapperConfiguration configuration

    @Autowired
    ModelMapper modelMapper

    @Autowired
    CommandBus commandBus

    @Autowired
    EventBus eventBus

    @Async
    void execute(@Valid TransactionDetails incomingTransaction) {
        Transaction transaction = checkTransactionExistsDomainService.execute(incomingTransaction.transactionHash)
        commandBus.executeAndWait(new UpdateTransactionStatusCommand(
            id: transaction.id, status: TransactionStatus.SUCCESS
        ))
        DltEvent event = modelMapper.map(
            incomingTransaction,
            configuration.mappers[transaction.eventName] as Class<? extends FailedDltEvent>
        ).tap { event ->
            event.blockTimestamp = OffsetDateTime.ofInstant(
                Instant.ofEpochSecond(incomingTransaction.blockTimestamp.toLong()),
                ZoneId.systemDefault()
            )
        }
        eventBus.publish(event)
    }

}
