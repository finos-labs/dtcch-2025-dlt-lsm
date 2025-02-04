package io.builders.demo.dtcc.domain.orchestrator.event

import io.builders.demo.core.event.TransactionEvent

class MintTokenUserOrderedEvent extends TransactionEvent {

    String token
    String user
    BigDecimal amount

}
