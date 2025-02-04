package io.builders.demo.dtcc.infrastructure.endpoint.createsettlement

import io.builders.demo.dtcc.infrastructure.endpoint.common.SettlementRequestModel

interface CreateSettlementPort {

    void createSettlements(List<SettlementRequestModel> settlements)

}
