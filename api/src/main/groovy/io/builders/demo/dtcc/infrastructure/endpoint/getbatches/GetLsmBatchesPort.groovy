package io.builders.demo.dtcc.infrastructure.endpoint.getbatches

import io.builders.demo.dtcc.infrastructure.endpoint.common.LsmBatchViewModel

interface GetLsmBatchesPort {

    List<LsmBatchViewModel> getLsmBatches()

}
