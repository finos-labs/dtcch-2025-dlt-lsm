package io.builders.demo.dtcc.infrastructure.endpoint.addbalance.model

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

class AddBalanceRequestModel {

    @NotNull
    Integer userId

    @NotNull
    BigDecimal amount

    @NotEmpty
    String tokenAddress

}
