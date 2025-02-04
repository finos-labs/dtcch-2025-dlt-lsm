package io.builders.demo.dtcc.infrastructure.endpoint.getsettlement

import io.builders.demo.dtcc.infrastructure.endpoint.common.SettlementViewModel

interface GetPendingSettlementsPort {

    List<SettlementViewModel> getPendingSettlements()

}
