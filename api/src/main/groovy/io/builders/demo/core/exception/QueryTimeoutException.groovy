package io.builders.demo.core.exception

class QueryTimeoutException extends RuntimeException {

    QueryTimeoutException() {
        super('Query response not received after waiting the configured time.')
    }

}
