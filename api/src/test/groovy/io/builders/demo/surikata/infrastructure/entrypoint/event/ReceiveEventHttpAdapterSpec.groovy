package io.builders.demo.surikata.infrastructure.entrypoint.event

import com.fasterxml.jackson.databind.ObjectMapper
import io.builders.demo.core.BaseSpecification
import io.builders.demo.core.RandomGenerator
import io.builders.demo.surikata.application.event.service.map.ContractEventServiceMapper
import net.consensys.eventeum.dto.event.ContractEventDetails
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ReceiveEventHttpAdapterSpec extends BaseSpecification {

    @SpringBean
    ContractEventServiceMapper appService = Mock()

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    MockMvc mockMvc

    void 'should receive an event and return status 202, when the endpoint is called'() {
        given:
        ContractEventDetails request = new ContractEventDetails(
            transactionHash: RandomGenerator.transactionHash(),
            address: RandomGenerator.address(),
            name: RandomGenerator.eventName()
        )

        when:
        ResultActions response = mockMvc.perform(post('/api/v1/surikata/contract-events')
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect(status().isAccepted())
        1 * appService.execute(_ as ContractEventDetails)
    }

}
