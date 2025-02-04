package io.builders.demo.surikata.domain.transaction.exception

import io.builders.demo.core.domain.exception.UnsupportedOperationDomainException

class TransactionAlreadyExistsDomainException extends UnsupportedOperationDomainException {

    static final String ALREADY_EXISTS_BY_HASH =
        'Transaction with hash %s already exists. #TRANSACTION_ALREADY_EXISTS_BY_HASH#'

    TransactionAlreadyExistsDomainException(String message) {
        super(message)
    }

}
