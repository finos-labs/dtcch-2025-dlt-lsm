package io.builders.demo.dtcc.application.dlt.query.getbalance;

import io.builders.demo.core.BaseSpecification
import io.builders.demo.core.query.QueryBus
import org.springframework.beans.factory.annotation.Autowired

class GetBalanceQueryHandlerSpec extends BaseSpecification {

    @Autowired
    QueryBus queryBus

    void 'test'() {
        when:
        queryBus.executeAndWait(new GetBalanceQuery(
            addresses: ['0xba2bfd3d2e5263f98b62bdd9941d9bcf3dcc7abd']
        ))
        then:
        noExceptionThrown()
    }

}
