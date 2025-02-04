package io.builders.demo.blockchain.domain.custody.exception

import io.builders.demo.core.domain.exception.EntityNotFoundDomainException

class CustodyKeyNotExistsDomainException extends EntityNotFoundDomainException {

    static final String DEFAULT_MESSAGE = 'Custody key with address %s does not exist. #CUSTODY_KEY_NOT_EXISTS#'

    CustodyKeyNotExistsDomainException(String address) {
        super(String.format(DEFAULT_MESSAGE, address))
    }

}
