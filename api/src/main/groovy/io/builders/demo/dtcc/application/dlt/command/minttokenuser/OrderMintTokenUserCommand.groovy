package io.builders.demo.dtcc.application.dlt.command.minttokenuser

import io.builders.demo.core.command.Command
import io.builders.demo.dtcc.domain.orchestrator.event.MintTokenUserOrderedEvent

class OrderMintTokenUserCommand extends Command<MintTokenUserOrderedEvent>{

    String tokenAddress
    String userAddress
    BigDecimal amount

}
