package io.builders.demo.dtcc.infrastructure.endpoint.addbalance

import io.builders.demo.dtcc.application.service.addbalance.AddBalanceAppService
import io.builders.demo.dtcc.application.service.addbalance.AddBalanceAppServiceModel
import io.builders.demo.dtcc.infrastructure.endpoint.addbalance.model.AddBalanceRequestModel
import jakarta.validation.Valid
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = '/api/v1/', produces = 'application/json')
@CrossOrigin(origins = '*')
class AddBalanceHttpAdapter implements AddBalancePort {

    @Autowired
    AddBalanceAppService appService

    @Autowired
    ModelMapper modelMapper

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping('/add-balance')
    void addBalance(@RequestBody @Valid AddBalanceRequestModel requestModel) {
        appService.execute(modelMapper.map(requestModel, AddBalanceAppServiceModel))
    }

}
