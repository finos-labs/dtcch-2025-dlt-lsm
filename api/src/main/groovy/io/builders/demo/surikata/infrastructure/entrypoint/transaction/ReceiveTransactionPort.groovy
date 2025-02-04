package io.builders.demo.surikata.infrastructure.entrypoint.transaction

import net.consensys.eventeum.dto.transaction.TransactionDetails
import org.springframework.http.ResponseEntity

interface ReceiveTransactionPort {

    ResponseEntity<Void> receiveTransaction(TransactionDetails event)

}
