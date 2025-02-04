package io.builders.demo.dtcc.infrastructure.endpoint.createsettlement

import io.builders.demo.dtcc.application.service.checkusersettlementcreation.CheckUserSettlementAppService
import io.builders.demo.dtcc.application.service.checkusersettlementcreation.CheckUserSettlementAppServiceModel
import io.builders.demo.dtcc.infrastructure.endpoint.common.SettlementRequestModel
import jakarta.validation.Valid
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = '/api/v1/', produces = 'application/json')
@CrossOrigin(origins = '*')
class CreateSettlementHttpAdapter implements CreateSettlementPort {

    @Autowired
    CheckUserSettlementAppService appService

    @Autowired
    ModelMapper modelMapper

    @PostMapping('/settlements')
    void createSettlements(@RequestBody @Valid List<SettlementRequestModel> settlements) {
        appService.execute(settlements.forEach { this.modelMapper.map(it, CheckUserSettlementAppServiceModel) })
    }

}
