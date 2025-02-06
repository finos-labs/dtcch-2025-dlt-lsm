package io.builders.demo.dtcc.infrastructure.endpoint.callbackai

import groovy.util.logging.Slf4j
import io.builders.demo.dtcc.application.service.manageairesponse.ManageAIResponseAppService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = '/api/v1/', produces = 'application/json')
@CrossOrigin(origins = '*')
@Slf4j
class CallbackAIHttpAdapter implements CallbackAIPort {

    @Autowired
    ManageAIResponseAppService appService

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping('/callback')
    void execute(@RequestBody List<Integer> settlements) {
        log.info("✅ Received settlements ${settlements}")
        appService.execute(settlements)
    }

}
