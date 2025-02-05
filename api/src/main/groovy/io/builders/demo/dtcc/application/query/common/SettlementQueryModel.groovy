package io.builders.demo.dtcc.application.query.common

import java.time.OffsetDateTime

class SettlementQueryModel {

    String buyerAlias
    String sellerAlias
    String status
    BigDecimal securityAmount
    BigDecimal cashAmount
    OffsetDateTime creationDate
    Integer id

}
