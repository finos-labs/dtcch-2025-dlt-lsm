package io.builders.demo.dtcc.domain.lsmbatch.exception

import io.builders.demo.core.domain.exception.EntityNotFoundDomainException

class LsmBatchNotFoundDomainException extends EntityNotFoundDomainException {

    static final String DEFAULT_MESSAGE = 'LsmBatch not found with id %s'

    LsmBatchNotFoundDomainException(Integer id) {
        super(String.format(DEFAULT_MESSAGE, id))
    }

}
