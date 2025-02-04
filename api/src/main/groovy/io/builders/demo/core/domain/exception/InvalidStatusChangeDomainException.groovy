package io.builders.demo.core.domain.exception

class InvalidStatusChangeDomainException extends RuntimeException {

    static final String DEFAULT_MESSAGE = 'Invalid status change from %s to %s. #STATUS_TRANSITION_NOT_VALID#'

    InvalidStatusChangeDomainException(String currentStatus, String nextStatus) {
        super(String.format(DEFAULT_MESSAGE, currentStatus, nextStatus))
    }

}
