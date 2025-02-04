package io.builders.demo.dtcc.infrastructure.endpoint.createsettlement

import io.builders.demo.dtcc.infrastructure.endpoint.common.SettlementRequestModel
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestBody

interface CreateSettlementPort {

    void createSettlements(@RequestBody @Valid List <SettlementRequestModel> settlements)

}
