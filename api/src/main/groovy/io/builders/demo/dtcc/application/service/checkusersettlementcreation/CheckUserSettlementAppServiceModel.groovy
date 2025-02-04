package io.builders.demo.dtcc.application.service.checkusersettlementcreation

import jakarta.validation.constraints.NotNull

class CheckUserSettlementAppServiceModel {

    @NotNull
    Integer buyerId

    @NotNull
    Integer sellerId

    @NotNull
    BigDecimal securityAmount

    @NotNull
    BigDecimal cashAmount

}
