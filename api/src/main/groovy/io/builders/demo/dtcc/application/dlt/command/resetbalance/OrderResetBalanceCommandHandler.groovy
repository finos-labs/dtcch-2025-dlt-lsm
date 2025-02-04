package io.builders.demo.dtcc.application.dlt.command.resetbalance

import io.builders.demo.blockchain.ContractLoader
import io.builders.demo.core.command.CommandHandler
import io.builders.demo.core.event.EventBus
import io.builders.demo.dtcc.application.dlt.FormatDecimals
import io.builders.demo.dtcc.domain.lsmbatch.event.ResetBalancesOrderedEvent
import jakarta.validation.Valid
import org.iobuilders.dtcc.contracts.DvpOrchestrator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.web3j.protocol.core.methods.response.TransactionReceipt

@Component
class OrderResetBalanceCommandHandler implements CommandHandler<ResetBalancesOrderedEvent, OrderResetBalanceCommand> {

    @Autowired
    ContractLoader contractLoader

    @Autowired
    EventBus eventBus

    @Override
    ResetBalancesOrderedEvent handle(@Valid OrderResetBalanceCommand command) {
        DvpOrchestrator contract = contractLoader.load(DvpOrchestrator)
        TransactionReceipt txReceipt = contract.send_resetBalances(command.addresses, FormatDecimals.toBigInteger(command.amount)).send()
        eventBus.publish(new ResetBalancesOrderedEvent(
            transactionId: txReceipt?.transactionHash,
            contractAddress: contract.contractAddress
        ))
    }
}
