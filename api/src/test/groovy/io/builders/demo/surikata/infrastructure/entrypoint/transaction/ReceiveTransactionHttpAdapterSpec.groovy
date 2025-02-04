package io.builders.demo.surikata.infrastructure.entrypoint.transaction

import com.fasterxml.jackson.databind.ObjectMapper
import io.builders.demo.core.BaseSpecification
import io.builders.demo.core.RandomGenerator
import io.builders.demo.surikata.application.transaction.service.mapper.TransactionServiceMapper
import net.consensys.eventeum.dto.transaction.TransactionDetails
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ReceiveTransactionHttpAdapterSpec extends BaseSpecification {

    @SpringBean
    TransactionServiceMapper appService = Mock()

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    MockMvc mockMvc

    void 'should receive a transaction and return status 202, when the endpoint is called'() {
        given:
        TransactionDetails request = new TransactionDetails(
            hash: RandomGenerator.transactionHash(),
            contractAddress: RandomGenerator.address(),
            revertReason: ''
        )

        when:
        ResultActions response = mockMvc.perform(post('/api/v1/surikata/transactions')
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect(status().isAccepted())
        1 * appService.execute(_ as TransactionDetails)
    }

}
