package io.builders.demo.dtcc.application.dlt.command.executelsm

import io.builders.demo.dtcc.application.dlt.common.Transaction
import io.builders.demo.dtcc.infrastructure.configuration.DtccConfigurationProperties
import io.builders.demo.core.BaseSpecification
import io.builders.demo.core.command.CommandBus
import org.springframework.beans.factory.annotation.Autowired

class OrderExecuteLsmCommandHandlerSpec extends BaseSpecification {

    @Autowired
    CommandBus commandBus

    @Autowired
    DtccConfigurationProperties dtccConfigurationProperties

    void 'test'() {
        given:
        String walletA = '0xba2bfd3d2e5263f98b62bdd9941d9bcf3dcc7abd'
        String walletB = '0x165330a1b4383bb2a0a197db53984535e44c7aa8'
        when:
        commandBus.executeAndWait(new OrderExecuteLsmCommand(
            transactions: [
                new Transaction(fromAddress: walletA, toAddress: walletB,
                    amount: BigDecimal.ONE, tokenAddress: dtccConfigurationProperties.cashToken)
            ]
        ))
        then:
        noExceptionThrown()
    }

}
