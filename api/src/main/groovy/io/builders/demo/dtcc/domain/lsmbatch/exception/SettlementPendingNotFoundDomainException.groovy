package io.builders.demo.dtcc.domain.lsmbatch.exception

import io.builders.demo.core.domain.exception.EntityNotFoundDomainException

class SettlementPendingNotFoundDomainException extends EntityNotFoundDomainException {

    static final String DEFAULT_MESSAGE = 'There are not pending settlement to execute process'

    SettlementPendingNotFoundDomainException() {
        super(DEFAULT_MESSAGE)
    }

}
