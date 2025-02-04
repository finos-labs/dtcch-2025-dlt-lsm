package io.builders.demo.surikata.domain.transaction.event

import io.builders.demo.core.event.Event

class TransactionCreatedEvent implements Event {

    UUID id
    String hash
    String contractAddress

}
