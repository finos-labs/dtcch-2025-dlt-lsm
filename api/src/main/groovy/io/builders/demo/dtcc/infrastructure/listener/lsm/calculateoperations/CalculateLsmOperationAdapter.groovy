package io.builders.demo.dtcc.infrastructure.listener.lsm.calculateoperations

import io.builders.demo.dtcc.application.service.calculatelsmoperations.CalculateLsmOperationsAppService
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmNetCalculatedEvent
import org.springframework.beans.factory.annotation.Autowired

class CalculateLsmOperationAdapter implements CalculateLsmOperationsPort {

    @Autowired
    CalculateLsmOperationsAppService appservice

    @Override
    void receive(LsmNetCalculatedEvent event) {
        appservice.calculate(event.lsmId)
    }

}
