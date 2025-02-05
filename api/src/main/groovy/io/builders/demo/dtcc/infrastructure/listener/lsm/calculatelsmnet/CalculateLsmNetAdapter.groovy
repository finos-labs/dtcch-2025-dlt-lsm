package io.builders.demo.dtcc.infrastructure.listener.lsm.calculatelsmnet

import io.builders.demo.dtcc.application.service.calculatelsmnet.CalculateLsmNetAppService
import io.builders.demo.dtcc.application.service.calculatelsmnet.CalculateLsmNetAppServiceModel
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmBatchStartedEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CalculateLsmNetAdapter implements CalculateLsmNetPort {

    @Autowired
    CalculateLsmNetAppService appService

    @Override
    void receive(LsmBatchStartedEvent event) {
        appService.execute(new CalculateLsmNetAppServiceModel(
                batchId: event.id,
        ))
    }

}
