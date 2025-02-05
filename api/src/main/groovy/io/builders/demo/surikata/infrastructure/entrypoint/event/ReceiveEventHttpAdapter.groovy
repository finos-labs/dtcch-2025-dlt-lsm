package io.builders.demo.surikata.infrastructure.entrypoint.event

import io.builders.demo.surikata.application.event.service.map.ContractEventServiceMapper
import net.consensys.eventeum.dto.event.ContractEventDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ReceiveEventHttpAdapter implements ReceiveEventPort {

    @Autowired
    ContractEventServiceMapper appService

    @Override
    @PostMapping('/api/v1/surikata/contract-events')
    ResponseEntity<Void> receiveEvent(@RequestBody ContractEventDetails event) {
        appService.execute(event)
        ResponseEntity.ok().build()
    }

}
