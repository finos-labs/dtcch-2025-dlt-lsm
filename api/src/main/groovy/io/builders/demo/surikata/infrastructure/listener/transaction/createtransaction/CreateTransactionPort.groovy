package io.builders.demo.surikata.infrastructure.listener.transaction.createtransaction

import io.builders.demo.core.event.EventListener
import io.builders.demo.core.event.TransactionEvent

interface CreateTransactionPort extends EventListener<TransactionEvent> {

}
