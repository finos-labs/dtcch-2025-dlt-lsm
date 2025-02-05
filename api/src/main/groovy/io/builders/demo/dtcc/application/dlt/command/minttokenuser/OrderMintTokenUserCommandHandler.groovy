package io.builders.demo.dtcc.application.dlt.command.minttokenuser

import io.builders.demo.blockchain.ContractLoader
import io.builders.demo.core.command.CommandHandler
import io.builders.demo.core.event.EventBus
import io.builders.demo.dtcc.application.dlt.FormatDecimals
import io.builders.demo.dtcc.domain.orchestrator.event.MintTokenUserOrderedEvent
import jakarta.validation.Valid
import org.iobuilders.dtcc.contracts.DvpOrchestrator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.web3j.protocol.core.methods.response.TransactionReceipt

@Component
class OrderMintTokenUserCommandHandler implements CommandHandler<MintTokenUserOrderedEvent, OrderMintTokenUserCommand> {

    @Autowired
    ContractLoader contractLoader

    @Autowired
    EventBus eventBus

    @Override
    MintTokenUserOrderedEvent handle(@Valid OrderMintTokenUserCommand command) {
        DvpOrchestrator contract = contractLoader.load(DvpOrchestrator)
        TransactionReceipt txReceipt = contract.send_issueTokens(
            command.tokenAddress,
            command.userAddress,
            FormatDecimals.toBigInteger(command.amount)
        ).send()
        eventBus.publish(new MintTokenUserOrderedEvent(
            transactionId: txReceipt?.transactionHash,
            contractAddress: contract.contractAddress,
            token: command.tokenAddress,
            user: command.userAddress,
            amount: command.amount
        ))
    }
}
