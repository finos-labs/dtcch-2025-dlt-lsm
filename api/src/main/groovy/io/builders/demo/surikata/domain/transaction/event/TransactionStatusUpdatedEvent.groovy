package io.builders.demo.surikata.domain.transaction.event

import io.builders.demo.core.event.Event

class TransactionStatusUpdatedEvent implements Event {

    UUID id
    String newStatus
    String previousStatus

}
