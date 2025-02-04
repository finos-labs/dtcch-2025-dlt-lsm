package io.builders.demo.dtcc.infrastructure.endpoint.common

import java.time.OffsetDateTime

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

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
