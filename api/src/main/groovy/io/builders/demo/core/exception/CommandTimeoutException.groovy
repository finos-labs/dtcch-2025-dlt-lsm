package io.builders.demo.core.exception

class CommandTimeoutException extends RuntimeException {

    CommandTimeoutException() {
        super('Command response not received after waiting the configured time.')
    }

}
