package io.builders.demo.dtcc.domain.lsmbatch.service

import io.builders.demo.dtcc.domain.lsmbatch.LsmBatchRepository
import io.builders.demo.dtcc.domain.lsmbatch.exception.LsmBatchNotFoundDomainException
import io.builders.demo.dtcc.domain.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CheckLsmBatchExistsDomainService {

    @Autowired
    LsmBatchRepository repository

    User execute(Integer id) throws LsmBatchNotFoundDomainException {
        repository.findById(id).orElseThrow {
            new LsmBatchNotFoundDomainException(id)
        }
    }

}
