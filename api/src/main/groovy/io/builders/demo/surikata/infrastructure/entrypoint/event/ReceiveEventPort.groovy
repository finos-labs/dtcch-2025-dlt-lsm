package io.builders.demo.surikata.infrastructure.entrypoint.event

import net.consensys.eventeum.dto.event.ContractEventDetails
import org.springframework.http.ResponseEntity

interface ReceiveEventPort {

    ResponseEntity<Void> receiveEvent(ContractEventDetails event)

}
