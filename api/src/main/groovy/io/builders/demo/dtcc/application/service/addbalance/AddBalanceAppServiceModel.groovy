package io.builders.demo.dtcc.application.service.addbalance

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

class AddBalanceAppServiceModel {

    @NotNull
    Integer userId

    @NotNull
    BigDecimal amount

    @NotEmpty
    String tokenAddress

}
