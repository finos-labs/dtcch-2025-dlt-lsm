package io.builders.demo.surikata.infrastructure.listener.transaction

import io.builders.demo.core.TestListener
import io.builders.demo.core.event.EventListener
import io.builders.demo.surikata.domain.transaction.event.TransactionCreatedEvent
import org.springframework.stereotype.Component

@Component
class TransactionTestListener implements TestListener {

    static final List<TransactionCreatedEvent> TRANSACTION_CREATED_EVENTS = []

    @Override
    void cleanup() {
        TRANSACTION_CREATED_EVENTS.clear()
    }

    @Component
    private class TransactionCreatedTestListener implements EventListener<TransactionCreatedEvent> {

        void receive(TransactionCreatedEvent event) {
            TRANSACTION_CREATED_EVENTS << event
        }

    }

}
