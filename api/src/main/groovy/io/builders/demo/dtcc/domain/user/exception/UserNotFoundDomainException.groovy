package io.builders.demo.dtcc.domain.user.exception

import io.builders.demo.core.domain.exception.EntityNotFoundDomainException

class UserNotFoundDomainException extends EntityNotFoundDomainException {

    static final String DEFAULT_MESSAGE = 'User not found with id %s'

    UserNotFoundDomainException(Integer id) {
        super(String.format(DEFAULT_MESSAGE, id))
    }
}
