package io.builders.demo.dtcc.infrastructure.endpoint.addbalance

import io.builders.demo.dtcc.infrastructure.endpoint.addbalance.model.AddBalanceRequestModel

interface AddBalancePort {

    void addBalance(AddBalanceRequestModel requestModel)

}
