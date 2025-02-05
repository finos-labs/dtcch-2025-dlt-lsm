package io.builders.demo.dtcc.application.dlt.command.executelsm

import io.builders.demo.blockchain.ContractLoader
import io.builders.demo.core.command.CommandHandler
import io.builders.demo.core.event.EventBus
import io.builders.demo.dtcc.application.dlt.FormatDecimals
import io.builders.demo.dtcc.domain.lsmbatch.event.ExecuteLsmOrderedEvent
import jakarta.validation.Valid
import org.iobuilders.dtcc.contracts.DvpOrchestrator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.web3j.protocol.core.methods.response.TransactionReceipt

@Component
class OrderExecuteLsmCommandHandler implements CommandHandler<ExecuteLsmOrderedEvent, OrderExecuteLsmCommand> {

    @Autowired
    ContractLoader contractLoader

    @Autowired
    EventBus eventBus

    @Override
    ExecuteLsmOrderedEvent handle(@Valid OrderExecuteLsmCommand command) {
        DvpOrchestrator contract = contractLoader.load(DvpOrchestrator)
        List<DvpOrchestrator.Transaction> transactions = command.transactions.collect { it ->
            new DvpOrchestrator.Transaction(
                it.fromAddress,
                it.toAddress,
                FormatDecimals.toBigInteger(it.amount).toInteger(),
                it.tokenAddress
            )
        }
        TransactionReceipt txReceipt = contract.send_executeLsm(
            command.batchId.toBigInteger(),
            transactions
            ).send()
        eventBus.publish(new ExecuteLsmOrderedEvent(
            transactionId: txReceipt?.transactionHash,
            contractAddress: contract.contractAddress,
            batchId: command.batchId
        ))
    }

}
