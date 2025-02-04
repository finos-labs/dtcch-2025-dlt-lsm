package io.builders.demo.api.infrastructure.endpoint.common

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import jakarta.validation.constraints.NotNull

@EqualsAndHashCode
@ToString
class SettlementRequestModel {

    @NotNull
    Integer buyerId

    @NotNull
    Integer sellerId

    @NotNull
    BigDecimal securityAmount

    @NotNull
    BigDecimal cashAmount

}
