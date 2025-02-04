package io.builders.demo.api.infrastructure.endpoint.common

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import jakarta.validation.constraints.NotNull

import java.time.OffsetDateTime

@EqualsAndHashCode
@ToString
class SettlementViewModel {

    Integer id
    Integer buyerId
    Integer sellerId
    BigDecimal securityAmount
    BigDecimal cashAmount
    OffsetDateTime creationDate

}
