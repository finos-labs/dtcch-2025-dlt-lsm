package io.builders.demo.dtcc.infrastructure.endpoint.resetbalances

import io.builders.demo.dtcc.application.service.resetbalance.ResetBalanceAppService
import io.builders.demo.dtcc.application.service.resetbalance.ResetBalanceAppServiceModel
import io.builders.demo.dtcc.infrastructure.endpoint.resetbalances.model.ResetBalanceRequestModel
import jakarta.validation.Valid
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = '/api/v1/', produces = 'application/json')
@CrossOrigin(origins = '*')
class ResetBalanceHttpAdapter implements ResetBalancePort {

    @Autowired
    ResetBalanceAppService appService

    @Autowired
    ModelMapper modelMapper

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping('/reset-balance')
    void resetBalance(@RequestBody @Valid ResetBalanceRequestModel requestModel) {
        appService.execute(modelMapper.map(requestModel, ResetBalanceAppServiceModel))
    }

}
