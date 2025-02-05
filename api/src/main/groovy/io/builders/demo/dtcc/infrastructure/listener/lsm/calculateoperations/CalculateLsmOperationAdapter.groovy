package io.builders.demo.dtcc.infrastructure.listener.lsm.calculateoperations

import io.builders.demo.dtcc.application.service.calculatelsmoperations.CalculateLsmOperationsAppService
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmNetCalculatedEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CalculateLsmOperationAdapter implements CalculateLsmOperationsPort {

    @Autowired
    CalculateLsmOperationsAppService appService

    @Override
    void receive(LsmNetCalculatedEvent event) {
        appService.calculate(event.lsmId)
    }

}
