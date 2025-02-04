package io.builders.demo.dtcc.infrastructure.endpoint.createsettlement

import io.builders.demo.dtcc.infrastructure.endpoint.common.SettlementsRequestModel

interface CreateSettlementPort {

    void createSettlements(SettlementsRequestModel settlements)

}
