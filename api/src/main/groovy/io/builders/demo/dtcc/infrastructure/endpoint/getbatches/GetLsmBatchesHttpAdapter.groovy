package io.builders.demo.dtcc.infrastructure.endpoint.getbatches

import io.builders.demo.core.query.QueryBus
import io.builders.demo.dtcc.application.query.getbatches.GetLsmBatchesQuery
import io.builders.demo.dtcc.infrastructure.endpoint.common.LsmBatchViewModel
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = '/api/v1/', produces = 'application/json')
@CrossOrigin(origins = '*')
class GetLsmBatchesHttpAdapter implements GetLsmBatchesPort {

    @Autowired
    ModelMapper modelMapper

    @Autowired
    QueryBus queryBus

    @GetMapping('/batches')
    List<LsmBatchViewModel> getLsmBatches() {
        this.queryBus.executeAndWait(new GetLsmBatchesQuery()).collect { this.modelMapper.map(it, LsmBatchViewModel) }
    }

}
