package io.builders.demo.dtcc.domain.lsmbatch.service

import io.builders.demo.dtcc.domain.lsmbatch.LsmBatch
import io.builders.demo.dtcc.domain.lsmbatch.LsmBatchRepository
import io.builders.demo.dtcc.domain.lsmbatch.exception.LsmBatchNotFoundDomainException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CheckLsmBatchExistsDomainService {

    @Autowired
    LsmBatchRepository repository

    LsmBatch execute(Integer id) throws LsmBatchNotFoundDomainException {
        repository.findById(id).orElseThrow {
            new LsmBatchNotFoundDomainException(id)
        }
    }

    LsmBatch executeBySettlementId(Integer settlementId) throws LsmBatchNotFoundDomainException {
        repository.findBySettlementIdIn(id).orElseThrow {
            new LsmBatchNotFoundDomainException(id)
        }
    }

}
