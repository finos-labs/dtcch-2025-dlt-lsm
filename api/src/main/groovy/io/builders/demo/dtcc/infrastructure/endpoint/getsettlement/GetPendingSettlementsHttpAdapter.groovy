package io.builders.demo.dtcc.infrastructure.endpoint.getsettlement

import io.builders.demo.core.query.QueryBus
import io.builders.demo.dtcc.application.query.getpendingsettlements.GetPendingSettlementsQuery
import io.builders.demo.dtcc.infrastructure.endpoint.common.SettlementViewModel
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = '/api/v1/', produces = 'application/json')
@CrossOrigin(origins = '*')
class GetPendingSettlementsHttpAdapter implements GetPendingSettlementsPort {

    @Autowired
    ModelMapper modelMapper

    @Autowired
    QueryBus queryBus

    @GetMapping('/pending-settlements')
    List<SettlementViewModel> getPendingSettlements() {
        this.queryBus.executeAndWait(new GetPendingSettlementsQuery())
            .collect {
            this.modelMapper.map(it, SettlementViewModel)
        }
    }

}
