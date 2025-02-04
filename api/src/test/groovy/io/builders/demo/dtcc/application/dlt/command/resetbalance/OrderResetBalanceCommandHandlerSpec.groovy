package io.builders.demo.dtcc.application.dlt.command.resetbalance

import io.builders.demo.core.BaseSpecification
import io.builders.demo.core.command.CommandBus
import org.springframework.beans.factory.annotation.Autowired

class OrderResetBalanceCommandHandlerSpec extends BaseSpecification {

    @Autowired
    CommandBus commandBus

    void 'test'() {
        when:
        commandBus.executeAndWait(new OrderResetBalanceCommand(
            addresses: ['0xba2bfd3d2e5263f98b62bdd9941d9bcf3dcc7abd'],
            amount: BigDecimal.valueOf(100))
        )
        then:
        noExceptionThrown()
    }

}

