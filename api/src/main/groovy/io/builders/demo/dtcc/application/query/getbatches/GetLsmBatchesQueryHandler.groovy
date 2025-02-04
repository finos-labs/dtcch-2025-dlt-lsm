package io.builders.demo.dtcc.application.query.getbatches

import io.builders.demo.core.query.QueryHandler
import io.builders.demo.dtcc.application.query.common.GetLsmBatchesQueryModel
import io.builders.demo.dtcc.domain.lsmbatch.LsmBatch
import io.builders.demo.dtcc.domain.lsmbatch.LsmBatchRepository
import jakarta.validation.Valid
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetLsmBatchesQueryHandler implements QueryHandler<List<GetLsmBatchesQueryModel>, GetLsmBatchesQuery> {

    @Autowired
    LsmBatchRepository lsmBatchRepository

    @Autowired
    ModelMapper modelMapper

    @Override
    List<GetLsmBatchesQueryModel> handle(@Valid GetLsmBatchesQuery query) {
        List<LsmBatch> batches = this.lsmBatchRepository.findAllWithSettlements()

        batches.collect {
            this.modelMapper.map(it, GetLsmBatchesQueryModel)
        }
    }
}
