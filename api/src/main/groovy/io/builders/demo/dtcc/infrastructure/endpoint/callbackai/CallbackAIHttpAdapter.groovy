package io.builders.demo.dtcc.infrastructure.endpoint.callbackai

import io.builders.demo.dtcc.application.service.manageairesponse.ManageAIResponseAppService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = '/api/v1/', produces = 'application/json')
@CrossOrigin(origins = '*')
class CallbackAIHttpAdapter implements CallbackAIPort {

    @Autowired
    ManageAIResponseAppService appService

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping('/callback')
    void execute(@RequestBody List<Integer> settlements) {
        appService.execute(settlements)
    }

}
