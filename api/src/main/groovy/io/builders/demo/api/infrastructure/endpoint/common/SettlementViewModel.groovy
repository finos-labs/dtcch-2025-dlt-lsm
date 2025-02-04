package io.builders.demo.api.infrastructure.endpoint.common

import java.time.OffsetDateTime

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

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
