package io.builders.demo.surikata.application.event.service.map

import io.builders.demo.core.command.CommandBus
import io.builders.demo.core.event.DltEvent
import io.builders.demo.core.event.EventBus
import io.builders.demo.surikata.application.transaction.command.updatetransactionstatus.UpdateTransactionStatusCommand
import io.builders.demo.surikata.domain.transaction.Transaction
import io.builders.demo.surikata.domain.transaction.TransactionRepository
import io.builders.demo.surikata.domain.transaction.TransactionStatus
import io.builders.demo.surikata.domain.transaction.service.CheckTransactionExistsDomainService
import io.builders.demo.surikata.infrastructure.configuration.SurikataEventMapperConfiguration
import jakarta.validation.Valid
import net.consensys.eventeum.dto.event.ContractEventDetails
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
class ContractEventServiceMapper {

    @Autowired
    CheckTransactionExistsDomainService checkTransactionExistsDomainService

    @Autowired
    SurikataEventMapperConfiguration surikataMapperConfiguration

    @Autowired
    ModelMapper modelMapper

    @Autowired
    CommandBus commandBus

    @Autowired
    EventBus eventBus

    @Autowired
    TransactionRepository repository

    @Async
    void execute(@Valid ContractEventDetails contractEvent) {
        Optional<Transaction> transaction = repository.findByHash(contractEvent.transactionHash)
        if (transaction.isPresent()) {
            Transaction tx = transaction.get()
            commandBus.executeAndWait(new UpdateTransactionStatusCommand(
                id: tx.id, status: TransactionStatus.SUCCESS
            ))
            DltEvent event = modelMapper.map(
                contractEvent,
                surikataMapperConfiguration.mappers[contractEvent.name] as Class<? extends DltEvent>
            )
            eventBus.publish(event)
        }
    }

}
