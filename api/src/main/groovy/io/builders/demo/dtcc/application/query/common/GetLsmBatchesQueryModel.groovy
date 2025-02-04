package io.builders.demo.dtcc.application.query.common

import java.time.OffsetDateTime

class GetLsmBatchesQueryModel {
    Integer id
    OffsetDateTime executionDate
    String aiResult
    List<LsmBatchSettlementQueryModel> settlements
}
