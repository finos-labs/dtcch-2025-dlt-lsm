package io.builders.demo.dtcc.application.query.common

import java.time.OffsetDateTime

class LsmBatchSettlementQueryModel {

    Integer id
    String buyerAlias
    String sellerAlias
    String status
    BigDecimal securityAmount
    BigDecimal cashAmount
    OffsetDateTime creationDate

}
