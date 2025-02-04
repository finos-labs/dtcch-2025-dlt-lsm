package io.builders.demo.dtcc.infrastructure.endpoint.common

import java.time.OffsetDateTime

class LsmBatchViewModel {
    Integer id
    OffsetDateTime executionDate
    String aiResult
    List<SettlementViewModel> settlements
}
