package io.builders.demo.surikata.domain.transaction.exception

import io.builders.demo.core.domain.exception.EntityNotFoundDomainException

class TransactionNotExistsDomainException extends EntityNotFoundDomainException {

    static final String NOT_EXIST_BY_HASH_MESSAGE =
        'Transaction with hash %s does not exists. #TRANSACTION_NOT_EXISTS_BY_HASH#'

    static final String NOT_EXIST_BY_ID_MESSAGE =
        'Transaction %s does not exists. #TRANSACTION_NOT_EXISTS#'

    TransactionNotExistsDomainException(String message) {
        super(message)
    }

}
