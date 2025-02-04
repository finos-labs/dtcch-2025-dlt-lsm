package io.builders.demo.surikata.infrastructure.entrypoint.transaction

import io.builders.demo.surikata.application.transaction.service.mapper.TransactionServiceMapper
import net.consensys.eventeum.dto.transaction.TransactionDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ReceiveTransactionHttpAdapter implements ReceiveTransactionPort {

    @Autowired
    TransactionServiceMapper appService

    @Override
    @PostMapping('/api/v1/surikata/transactions')
    ResponseEntity<Void> receiveTransaction(@RequestBody TransactionDetails transaction) {
        appService.execute(transaction)
        ResponseEntity.accepted().build()
    }

}
