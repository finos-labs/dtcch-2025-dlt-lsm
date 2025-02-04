package io.builders.demo.api.infrastructure.endpoint.common

import jakarta.validation.constraints.NotNull

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

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
