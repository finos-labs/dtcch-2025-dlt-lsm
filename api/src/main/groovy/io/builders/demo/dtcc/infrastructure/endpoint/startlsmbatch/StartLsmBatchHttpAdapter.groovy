package io.builders.demo.dtcc.infrastructure.endpoint.startlsmbatch

import io.builders.demo.core.command.CommandBus
import io.builders.demo.dtcc.application.command.starlsmbatch.StartLsmBatchCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = '/api/v1/', produces = 'application/json')
@CrossOrigin(origins = '*')
class StartLsmBatchHttpAdapter implements StartLsmBatchPort {

    @Autowired
    CommandBus commandBus

    @Override
    @PostMapping('batches')
    void start() {
        commandBus.executeAndWait(new StartLsmBatchCommand())
    }

}
