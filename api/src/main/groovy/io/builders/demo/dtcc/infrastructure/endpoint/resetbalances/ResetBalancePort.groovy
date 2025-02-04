package io.builders.demo.dtcc.infrastructure.endpoint.resetbalances

import io.builders.demo.dtcc.infrastructure.endpoint.resetbalances.model.ResetBalanceRequestModel

interface ResetBalancePort {

    void resetBalance(ResetBalanceRequestModel amount)

}
