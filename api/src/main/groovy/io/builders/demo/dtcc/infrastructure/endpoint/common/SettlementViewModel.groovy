package io.builders.demo.dtcc.infrastructure.endpoint.common

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.OffsetDateTime

@EqualsAndHashCode
@ToString
class SettlementViewModel {

    Integer id
    String buyer
    String seller
    String status
    BigDecimal securityAmount
    BigDecimal cashAmount
    OffsetDateTime creationDate

}
