package io.builders.demo.core.exception

class CommandHandlerNotFoundException extends RuntimeException {

    CommandHandlerNotFoundException(String command) {
        super(String.format('Command handler not found for command %s.', command))
    }

}
