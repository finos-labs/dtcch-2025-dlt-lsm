package io.builders.demo.api.domain.settlement.event

import io.builders.demo.core.event.Event

class SettlementCreatedEvent implements Event {

    Integer id
    Integer buyerId
    Integer sellerId
    BigDecimal securityAmount
    BigDecimal cashAmount

}
