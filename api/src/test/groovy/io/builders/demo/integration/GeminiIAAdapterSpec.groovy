package io.builders.demo.integration

import io.builders.demo.core.BaseSpecification
import io.builders.demo.integration.model.Balance
import io.builders.demo.integration.model.IARequest
import io.builders.demo.integration.model.IASettlement
import org.springframework.beans.factory.annotation.Autowired

class GeminiIAAdapterSpec extends BaseSpecification {
    @Autowired
    GeminiIAAdapter geminiIAAdapter

    void 'IA must return a response'() {
        given:
        List<String> clients = ['w', 'x', 'y', 'z']
        List<IASettlement> settlements = [
            IASettlement.create(BigDecimal.TEN, BigDecimal.ONE, clients[0], clients[1]),
            IASettlement.create(BigDecimal.TEN, BigDecimal.ONE, clients[1], clients[2]),
            IASettlement.create(BigDecimal.TEN, BigDecimal.ONE, clients[2], clients[3])
        ]
        Map<String, Balance> balancesMap = [
            (clients[0]): Balance.create(BigDecimal.TEN, BigDecimal.TEN),
            (clients[1]): Balance.create(BigDecimal.TEN, BigDecimal.TEN),
            (clients[2]): Balance.create(BigDecimal.TEN, BigDecimal.TEN),
            (clients[3]): Balance.create(BigDecimal.TEN, BigDecimal.TEN)
        ]
        when:
        List<String> combination = geminiIAAdapter.obtainIACombination(new IARequest(settlements: settlements, balances: balancesMap))

        then:
        noExceptionThrown()
        combination == ["AA", "AB", "AC"]
    }

}
